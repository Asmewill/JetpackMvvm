package com.example.oapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.adapter.KnowledgeItemAdapter
import com.example.oapp.base.BaseVmDbFragment
import com.example.oapp.bean.KnowItemListBean
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.FragmentKnowTreeBinding
import com.example.oapp.ext.showToast
import com.example.oapp.ui.ContentActivity
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.CollectViewModel
import com.example.oapp.viewmodel.KnowledgeItemViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.activity_point_rank_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerView
import kotlinx.android.synthetic.main.fragment_home.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_know_tree.*
import kotlinx.android.synthetic.main.fragment_wechat.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback


/**
 * Created by jsxiaoshui on 2021/8/2
 */
class KnowledgeItemFragment: BaseVmDbFragment<KnowledgeItemViewModel, FragmentKnowTreeBinding>() {
    lateinit var loadService: LoadService<Any>
    private val collectViewModel:CollectViewModel by viewModels()
    private var cid=0
    private var pageNo=0
    private val knowledgeItemAdapter by lazy {
       KnowledgeItemAdapter(collectViewModel)
    }

    companion object{
        fun getInstance(cid:Int):KnowledgeItemFragment{
            val instance=KnowledgeItemFragment()
            val bundle=Bundle()
            bundle.putInt(Constant.TAB_CID,cid)
            instance.arguments=bundle
            return instance
        }
    }
    override fun layoutId(): Int {
        return R.layout.fragment_know_tree
    }

    override fun createViewModel(): KnowledgeItemViewModel {
        
       return ViewModelProvider(this).get(KnowledgeItemViewModel::class.java)
    }

    override fun initView() {
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback::class.java)
            pageNo=0
            mViewModel.getKnowledgeItemList(pageNo,cid)
        }
        cid=arguments?.getInt(Constant.TAB_CID)?:0
        recyclerView?.let {
            it.layoutManager=LinearLayoutManager(activity)
            it.adapter=knowledgeItemAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            pageNo=0
            mViewModel.getKnowledgeItemList(pageNo,cid)
        }
        knowledgeItemAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val itemBean=baseQuickAdapter.getItem(i) as KnowItemListBean.Data
            ARouter.getInstance().build(Constant.PagePath.CONTENT)
                .withString(Constant.CONTENT_URL,itemBean.link)
                .withString(Constant.CONTENT_TITLE,itemBean.title)
                .withInt(Constant.CONTENT_ID,itemBean.id)
                .navigation()

        }
        knowledgeItemAdapter.setOnLoadMoreListener({
            if(knowledgeItemAdapter.data.size<8){
                return@setOnLoadMoreListener
            }
            pageNo++
            mViewModel.getKnowledgeItemList(pageNo,cid)

        },recyclerView)
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getKnowledgeItemList(pageNo,cid)
    }

    override fun createObserver() {
        addLoadingObserve(collectViewModel)
        mViewModel.knowItemLiveData.observe(this) {
            swipeRefreshLayout?.isRefreshing=false
            when(!it.isException){
                true->{
                    if (it.dataBean != null && it.dataBean?.data?.datas != null &&
                        it.dataBean?.data?.datas?.size ?: 0 > 0) {//数据为null
                        if (pageNo <= 0) {//第一页
                            loadService.showCallback(SuccessCallback::class.java)
                            it.dataBean?.data?.datas?.let { beanList ->
                                knowledgeItemAdapter.setNewData(beanList)
                            }
                        } else {//第二三....页
                            it.dataBean?.data?.datas?.let { beanList ->
                                knowledgeItemAdapter.addData(beanList)
                            }
                            //强制停止RecycleView的滑动
                            recyclerView?.let {
                                CommonUtil.forceStopRecycleViewScroll(it)
                            }
                        }
                        //设置是否可以加载更多
                        if (it.dataBean?.data?.curPage!! < it.dataBean?.data?.pageCount!!) {
                            knowledgeItemAdapter.loadMoreComplete()
                            knowledgeItemAdapter.setEnableLoadMore(true)
                        } else {
                            knowledgeItemAdapter.loadMoreEnd(false)
                        }
                    } else {//数据不为null
                        if (pageNo <= 0) {//第一页,显示空布局
                            loadService.showCallback(EmptyCallback::class.java)
                        }
                    }
                }
                false->{
                    if (pageNo > 0) {
                        knowledgeItemAdapter.loadMoreFail()//上拉加载,第二页之后，加载失败时显示异常
                        pageNo--
                    } else {
                        loadService.showCallback(ErrorCallback::class.java)
                    }
                    it.error?.message?.let { it1 -> showToast(it1) }
                }
            }
        }

        collectViewModel.addCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("收藏成功")
                        knowledgeItemAdapter.getItem(it.position)?.collect=true
                        knowledgeItemAdapter.notifyDataSetChanged()
                    }else{
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false->{
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })
        collectViewModel.cancelCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("已取消收藏")
                        knowledgeItemAdapter.getItem(it.position)?.collect=false
                        knowledgeItemAdapter.notifyDataSetChanged()
                    }else{
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false->{
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })

    }
}
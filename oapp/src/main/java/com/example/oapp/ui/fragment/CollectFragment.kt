package com.example.oapp.ui.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.adapter.CollectListAdapter
import com.example.oapp.base.BaseVmDbFragment
import com.example.oapp.bean.CollectArticle
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.FragmentCollectBinding
import com.example.oapp.ui.ContentActivity
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.CollectViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.*

/**
 * Created by jsxiaoshui on 2021/7/27
 */
class CollectFragment:BaseVmDbFragment<CollectViewModel,FragmentCollectBinding> (){
    private lateinit var loadService: LoadService<Any>
    private val list= mutableListOf<CollectArticle.Data>()
    private val collectListAdapter by lazy {
        CollectListAdapter(mViewModel,list)
    }
    private var pageNo: Int=0

    companion object{
        fun getInstance():CollectFragment{
            val instance=CollectFragment()
            return instance
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_collect
    }

    override fun createViewModel(): CollectViewModel {
        return ViewModelProvider(this).get(CollectViewModel::class.java)
    }


    override fun initView() {
        mDataBind.floatingActionBtn.backgroundTintList= ColorStateList.valueOf(mThemeColor)
        //设置ErrorCallback和EmptyCallback点击之后的回调逻辑处理
        loadService= LoadSir.getDefault().register(mDataBind.swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback2::class.java)
            pageNo=0
            mViewModel.getCollectList(pageNo)
        }
        //自定义修改ErrorCallback内部属性
        loadService.setCallBack(ErrorCallback::class.java) { p0, p1 ->
            val tv_retry: TextView = p1.findViewById(R.id.tv_retry)
            val tv_empty=p1.findViewById<TextView>(R.id.tv_empty)
            val loading_errorimg=p1.findViewById<ImageView>(R.id.loading_errorimg)
            tv_empty.text = "世界上最遥远的距离就是没网..."
            tv_retry.text = "重试"
            loading_errorimg.setImageResource(R.drawable.ic_custom_drawable)
        }
        //自定义修改EmptyCallback内部属性
        loadService.setCallBack(EmptyCallback::class.java) { p0, p1 ->
            val tv_retry = p1.findViewById<TextView>(R.id.tv_retry)
            val tv_empty=p1.findViewById<TextView>(R.id.tv_empty)
            val loading_emptyimg=p1.findViewById<ImageView>(R.id.loading_emptyimg)
            tv_retry.text = "重试"
            tv_empty.text = "空空如也..."
            loading_emptyimg.setImageResource(R.drawable.ic_custom_drawable)
        }

       mDataBind.recyclerView?.let {
            it.layoutManager=LinearLayoutManager(activity)
            it.adapter=collectListAdapter
        }
        collectListAdapter.setOnItemClickListener {
                baseQuickAdapter, view, i ->
               val list= baseQuickAdapter.data as MutableList<CollectArticle.Data>
               val item=list.get(i)
               ARouter.getInstance().build(Constant.PagePath.CONTENT)
                   .withString(Constant.CONTENT_TITLE,item.title)
                   .withString(Constant.CONTENT_URL,item.link)
                   .withInt(Constant.CONTENT_ID,item.courseId)
                   .navigation()
        }
        //下拉刷新
        mDataBind.swipeRefreshLayout.setOnRefreshListener {
            pageNo=0
            mViewModel.getCollectList(pageNo)
        }
        //上拉加载更多
        collectListAdapter.setOnLoadMoreListener({
            //防止少于1页的时候，上拉加载的loading闪现，自动请求下一页数据
            if(collectListAdapter.data!=null&&collectListAdapter.data.size<8){
                return@setOnLoadMoreListener
            }
            pageNo++
            mViewModel.getCollectList(pageNo)

        },mDataBind.recyclerView)
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback2::class.java)
        mViewModel.getCollectList(pageNo)
    }

    override fun createObserver() {
        mViewModel.collectLiveData.observe(this, Observer {
            mDataBind.swipeRefreshLayout?.isRefreshing=false
            when(!it.isException){
                true->{
                    if(it.dataBean!=null&&it.dataBean?.data?.datas!=null&&
                        it.dataBean?.data?.datas?.size?:0>0){//数据为null
                        if(pageNo<=0){//第一页
                            loadService.showCallback(SuccessCallback::class.java)
                            it.dataBean?.data?.datas?.let { beanList->
                                collectListAdapter.setNewData(beanList.filter { item->
                                    item.collect=true//在Data中设置无效
                                    if(!TextUtils.isEmpty(item.envelopePic)){
                                        item.item_type=2
                                    }else{
                                        item.item_type=1
                                    }
                                    true
                                })
                            }
                        }else{//第二三....页
                            it.dataBean?.data?.datas?.let { beanList->
                                collectListAdapter.addData(beanList.filter { item->
                                    item.collect=true//在Data中设置无效,所以手动添加
                                    //使用多ItemType布局
                                    if(!TextUtils.isEmpty(item.envelopePic)){
                                        item.item_type=2
                                    }else{
                                        item.item_type=1
                                    }
                                    true
                                })
                            }
                            //强制停止RecycleView的滑动
                           mDataBind.recyclerView?.let {
                                CommonUtil.forceStopRecycleViewScroll(it)
                            }
                        }
                        //设置是否可以加载更多
                         if(it.dataBean?.data?.curPage!!<it.dataBean?.data?.pageCount!!){
                            collectListAdapter.loadMoreComplete()
                             collectListAdapter.setEnableLoadMore(true)
                        }else{
                             collectListAdapter.loadMoreEnd(false)
                        }
                    }else{//数据不为null
                        if(pageNo<=0){//第一页
                            loadService.showCallback(EmptyCallback::class.java)
                        }
                    }
                }
                false->{
                    if(pageNo>0){
                        collectListAdapter.loadMoreFail()//第二页之后，加载失败时显示异常
                        pageNo--
                    }else{
                        loadService.showCallback(ErrorCallback::class.java)
                    }
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })
        //添加收藏
        mViewModel.addCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                      CommonUtil.showToast("收藏成功")
                        collectListAdapter.getItem(it.position)?.collect=true
                        collectListAdapter.notifyDataSetChanged()
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
        //取消收藏
        mViewModel.cancelCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("已取消收藏")
                        collectListAdapter.getItem(it.position)?.collect=false
                        collectListAdapter.notifyDataSetChanged()
                        //collectListAdapter.remove(it.position)
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
package com.example.oapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.oapp.R
import com.example.oapp.adapter.WeChatItemAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.WeChatItemData
import com.example.oapp.constant.Constant
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.ui.ContentActivity
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.CollectViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback


/**
 * Created by jsxiaoshui on 2021/6/30
 */
class WeChatItemFragment:BaseFragment() {
    lateinit var loadService: LoadService<Any>
    private val mViewModel:CollectViewModel by viewModels()
    private var pageNo=0
    lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    lateinit var  recyclerView: RecyclerView
    private val weChatItemAdapter by lazy {
        WeChatItemAdapter(mViewModel)
    }
    private var cid: Int=-1
    companion object{
        fun getInstance(cid:Int):WeChatItemFragment{
            val weChatItemFragment=WeChatItemFragment();
            val bundle=Bundle();
            bundle.putInt(Constant.TAB_CID,cid)
            weChatItemFragment.arguments=bundle
            return weChatItemFragment
        }
    }
    override fun attachlayoutRes(): Int {
        return R.layout.fragment_wechat_tab
    }
    override fun initView() {
        swipeRefreshLayout=mContentView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        recyclerView=mContentView.findViewById<RecyclerView>(R.id.recyclerView)
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback::class.java)
            pageNo=0
            getWeChatItemData()
        }
        cid= arguments?.getInt(Constant.TAB_CID,-1)!!
        recyclerView?.run {
            this.layoutManager=LinearLayoutManager(activity)
            this.adapter=weChatItemAdapter
        }
        swipeRefreshLayout?.setOnRefreshListener {
            pageNo=0
            getWeChatItemData()
        }
        weChatItemAdapter.setOnLoadMoreListener( {
            if(weChatItemAdapter.data.size<8){
                return@setOnLoadMoreListener
            }
            pageNo++
            getWeChatItemData()
        },recyclerView)
        weChatItemAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val item:WeChatItemData.DatasBean = baseQuickAdapter.data.get(i) as WeChatItemData.DatasBean
            val intent=Intent(activity,ContentActivity::class.java)
            intent.putExtra(Constant.CONTENT_ID,item.id)
            intent.putExtra(Constant.CONTENT_URL,item.link)
            intent.putExtra(Constant.CONTENT_TITLE,item.title)
            startActivity(intent)

        }


    }
    override fun initData() {
        getWeChatItemData()
        mViewModel.addCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("收藏成功")
                        weChatItemAdapter.getItem(it.position)?.collect=true
                        weChatItemAdapter.notifyDataSetChanged()
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

        mViewModel.cancelCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("已取消收藏")
                        weChatItemAdapter.getItem(it.position)?.collect=false
                        weChatItemAdapter.notifyDataSetChanged()
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

    private fun getWeChatItemData() {
        HttpRetrofit.apiService.getWechatTabDetail(pageNo,cid).applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<WeChatItemData>>{
            override fun onSuccess(dataBean: HttpResult<WeChatItemData>) {
                 swipeRefreshLayout?.isRefreshing=false
                if (dataBean.data != null && dataBean.data?.datas != null &&
                    dataBean.data?.datas?.size ?: 0 > 0) {//数据为null
                    if (pageNo <= 0) {//第一页
                        loadService.showCallback(SuccessCallback::class.java)
                        dataBean?.data?.datas?.let { beanList ->
                            weChatItemAdapter.setNewData(beanList)
                        }
                    } else {//第二三....页
                        dataBean?.data?.datas?.let { beanList ->
                            weChatItemAdapter.addData(beanList)
                        }
                        //强制停止RecycleView的滑动
                        recyclerView?.let {
                            CommonUtil.forceStopRecycleViewScroll(it)
                        }
                    }
                    //设置是否可以加载更多
                    if (dataBean?.data?.curPage!! < dataBean?.data?.pageCount!!) {
                        weChatItemAdapter.loadMoreComplete()
                        weChatItemAdapter.setEnableLoadMore(true)
                    } else {
                        weChatItemAdapter.loadMoreEnd(false)
                    }
                } else {//数据不为null
                    if (pageNo <= 0) {//第一页,显示空布局
                        loadService.showCallback(EmptyCallback::class.java)
                    }
                }
            }
            override fun onFailture(error: Throwable) {
                swipeRefreshLayout?.isRefreshing=false
                if (pageNo > 0) {
                    weChatItemAdapter.loadMoreFail()//上拉加载,第二页之后，加载失败时显示异常
                    pageNo--
                } else {
                    loadService.showCallback(ErrorCallback::class.java)
                }
                error?.message?.let {
                    showToast(it)
                }
            }
        }))
    }
}
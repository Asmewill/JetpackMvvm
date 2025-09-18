package com.example.oapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.adapter.ProjectListAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.ProjectItemData
import com.example.oapp.constant.Constant
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.ui.ContentActivity
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.CollectViewModel
import com.example.oapp.viewmodel.EventViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback


/**
 * Created by jsxiaoshui on 2021/6/30
 */
class ProjectItemFragment:BaseFragment() {
    lateinit var loadService: LoadService<Any>
    private val mViewModel:CollectViewModel by viewModels()
    private var pageNo=1

    private var cid: Int=-1

    lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    lateinit var  recyclerView: RecyclerView
    private val projectListAdapter by lazy {
        ProjectListAdapter(mViewModel)
    }
    companion object{
        fun getInstance(cid:Int):ProjectItemFragment{
            val projectFragment=ProjectItemFragment();
            val bundle=Bundle()
            bundle.putInt(Constant.TAB_CID,cid)
            projectFragment.arguments=bundle
            return projectFragment
        }
    }
    override fun attachlayoutRes(): Int {
        return R.layout.fragment_project_list
    }
    override fun initView() {
        swipeRefreshLayout=mContentView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        recyclerView=mContentView.findViewById<RecyclerView>(R.id.recyclerView);

        createObserver()
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback::class.java)
            pageNo=1
            getProjectTabDetail()
        }
        cid= arguments?.getInt(Constant.TAB_CID)!!
        recyclerView?.let {
            it.layoutManager=LinearLayoutManager(activity)
            it.adapter=projectListAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            pageNo=1
            getProjectTabDetail()
        }
        projectListAdapter.setOnLoadMoreListener {
            if(projectListAdapter.itemCount<8){
               return@setOnLoadMoreListener
            }
            pageNo++
            getProjectTabDetail()
        }
        projectListAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val bean= baseQuickAdapter.data.get(i) as ProjectItemData.DatasBean
            ARouter.getInstance().build(Constant.PagePath.CONTENT)
                .withString(Constant.CONTENT_TITLE,bean.title)
                .withString(Constant.CONTENT_URL,bean.link)
                .withInt(Constant.CONTENT_ID,bean.id)
                .navigation()
        }
    }



    override fun initData() {
        getProjectTabDetail()
        mViewModel.addCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("收藏成功")
                        projectListAdapter.getItem(it.position)?.collect=true
                        projectListAdapter.notifyDataSetChanged()
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
                        projectListAdapter.getItem(it.position)?.collect=false
                        projectListAdapter.notifyDataSetChanged()
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

    private fun getProjectTabDetail() {
        HttpRetrofit.apiService.getProjectDetail(pageNo,cid).applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<ProjectItemData>>{
            override fun onSuccess(dataBean: HttpResult<ProjectItemData>) {
                swipeRefreshLayout?.isRefreshing=false
                if (dataBean.data != null && dataBean.data?.datas != null &&
                    dataBean.data?.datas?.size ?: 0 > 0) {//数据为null
                    if (pageNo <= 1) {//第一页
                        loadService.showCallback(SuccessCallback::class.java)
                        dataBean?.data?.datas?.let { beanList ->
                            projectListAdapter.setNewData(beanList)
                        }
                    } else {//第二三....页
                        dataBean?.data?.datas?.let { beanList ->
                            projectListAdapter.addData(beanList)
                        }
                        //强制停止RecycleView的滑动
                        recyclerView?.let {
                            CommonUtil.forceStopRecycleViewScroll(it)
                        }
                    }
                    //设置是否可以加载更多
                    if (dataBean?.data?.curPage!! < dataBean?.data?.pageCount!!) {
                        projectListAdapter.loadMoreComplete()
                        projectListAdapter.setEnableLoadMore(true)
                    } else {
                        projectListAdapter.loadMoreEnd(false)
                    }
                } else {//数据不为null
                    if (pageNo <= 1) {//第一页,显示空布局
                        loadService.showCallback(EmptyCallback::class.java)
                    }
                }
            }
            override fun onFailture(error: Throwable) {
                swipeRefreshLayout?.isRefreshing=false
                if (pageNo > 0) {
                    projectListAdapter.loadMoreFail()//上拉加载,第二页之后，加载失败时显示异常
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

    private fun createObserver() {
        EventViewModel.noPhotoLiveData.observeInFragment(this) {
            pageNo=1
            getProjectTabDetail()
        }
        EventViewModel.noPhotoLiveData.observeInFragment(this) {
            pageNo=1
            getProjectTabDetail()
        }
    }

}
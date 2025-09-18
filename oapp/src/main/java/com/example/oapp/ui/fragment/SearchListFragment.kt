package com.example.oapp.ui.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.oapp.R
import com.example.oapp.adapter.HomeAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.HomeData
import com.example.oapp.bean.HttpResult
import com.example.oapp.constant.Constant
import com.example.oapp.ext.applySchdules
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.ui.ContentActivity
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.CollectViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * Created by jsxiaoshui on 2021/7/8
 */
class SearchListFragment:BaseFragment() {
    private val mViewModel:CollectViewModel by viewModels()
    private var pageNum:Int=0
    private var keyWord:String=""
    lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    lateinit var  recyclerView: RecyclerView
    lateinit var floating_action_btn: FloatingActionButton

    private val homeAdapter by lazy {
        HomeAdapter(mViewModel)
    }

    override fun attachlayoutRes(): Int {
        return R.layout.fragment_search_list
    }
    companion object{
        fun getInstance(key:String?):SearchListFragment{
            val searchListFragment=SearchListFragment()
            val bundle=Bundle()
            bundle.putString(Constant.SEARCH_KEY,key)
            searchListFragment.arguments=bundle
            return searchListFragment
        }
    }

    override fun initView() {
        swipeRefreshLayout=mContentView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        recyclerView=mContentView.findViewById<RecyclerView>(R.id.recyclerView)
        floating_action_btn.findViewById<FloatingActionButton>(R.id.floating_action_btn)

        floating_action_btn.backgroundTintList= ColorStateList.valueOf(mThemeColor)
        keyWord=arguments?.getString(Constant.SEARCH_KEY)!!
        recyclerView?.let {
            it.layoutManager=LinearLayoutManager(activity)
            it.adapter=homeAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            pageNum=0
            getSearchList()
        }
        homeAdapter.setOnLoadMoreListener({
            pageNum++
            getSearchList()
        },recyclerView)
        homeAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { p0, _, p2 ->
                val intent=Intent(activity,ContentActivity::class.java)
                val bean:HomeData.DatasBean= p0?.data?.get(p2) as HomeData.DatasBean
                intent.putExtra(Constant.CONTENT_TITLE,bean.title)
                intent.putExtra(Constant.CONTENT_URL,bean.link)
                intent.putExtra(Constant.CONTENT_ID,bean.id)
                startActivity(intent)
            }
    }

    override fun initData() {
        getSearchList()
        mViewModel.addCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("收藏成功")
                        homeAdapter.getItem(it.position+1)?.collect=true
                        homeAdapter.notifyDataSetChanged()
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
                        homeAdapter.getItem(it.position+1)?.collect=false
                        homeAdapter.notifyDataSetChanged()
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

    private fun getSearchList() {
            HttpRetrofit.apiService.getSearchList(pageNum, keyWord).applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<HomeData>>{
                override fun onSuccess(t: HttpResult<HomeData>) {
                    if(t?.data != null &&t.data?.size!!>0){
                        if (pageNum <= 0) {
                            homeAdapter.setNewData(t.data?.datas)
                        } else {
                            homeAdapter.addData(t.data?.datas!!)
                        }
                        if (t?.data?.curPage!! < t?.data?.pageCount!!) {
                            homeAdapter?.loadMoreComplete()
                            homeAdapter?.setEnableLoadMore(true)
                        } else {
                            homeAdapter?.loadMoreEnd(false)
                        }
                    }
                    CommonUtil.forceStopRecycleViewScroll(recyclerView)//防止recycleView惯性滑动
                    swipeRefreshLayout?.isRefreshing=false
                }
                override fun onFailture(t: Throwable) {
                    swipeRefreshLayout?.isRefreshing=false
                }
            }))
        }

}
package com.example.oapp.ui

import android.content.Context
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.oapp.R
import com.example.oapp.adapter.RankListAdapter
import com.example.oapp.base.BaseVmDbActivity
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.PointBean
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.ActivityPointRankListBinding
import com.example.oapp.ext.showToast
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.RankListViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.kingja.loadsir.core.Transport
import kotlinx.android.synthetic.main.activity_point_rank_list.*
import kotlinx.android.synthetic.main.activity_point_rank_list.recyclerView
import kotlinx.android.synthetic.main.activity_point_rank_list.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback2

/**
 * Created by jsxiaoshui on 2021/7/22
 */
@Route(path = Constant.PagePath.RANKLIST)
class RankListActivity : BaseVmDbActivity<RankListViewModel, ActivityPointRankListBinding>() {

    var pageNo = 1
    lateinit var loadService: LoadService<Any>
    private val rankListAdapter by lazy {
        RankListAdapter()
    }

    override fun createViewModel(): RankListViewModel {
        return ViewModelProvider(this).get(RankListViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.activity_point_rank_list
    }

    override fun initView() {
        toolbar?.title = "积分排行榜"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//显示返回按钮
        recyclerView?.let {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = rankListAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            pageNo = 1
            mViewModel.getHomeRankList(pageNo)
        }
        rankListAdapter.setOnLoadMoreListener({
            pageNo++
            mViewModel.getHomeRankList(pageNo)
        }, recyclerView)
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback2::class.java)
            pageNo = 1
            mViewModel.getHomeRankList(pageNo)
        }
        //自定义修改
        loadService.setCallBack(
            ErrorCallback::class.java
        ) { p0, p1 ->
            val tv_retry: TextView = p1.findViewById(R.id.tv_retry)
            val tv_empty = p1.findViewById<TextView>(R.id.tv_empty)
            val loading_errorimg = p1.findViewById<ImageView>(R.id.loading_errorimg)
            tv_empty.text = "世界上最遥远的距离就是没网..."
            tv_retry.setText("点击当前页面,重试...")
            loading_errorimg.setImageResource(R.drawable.ic_custom_drawable)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback2::class.java)
        mViewModel.getHomeRankList(pageNo)
    }

    override fun createObserver() {
        mViewModel.rankListLiveData.observe(this,
            Observer<ListDataUiState<PointBean>> {
                swipeRefreshLayout?.isRefreshing = false
                when (!it.isException) {
                    true -> {
                        if (it.dataBean != null && it.dataBean?.data?.datas != null &&
                            it.dataBean?.data?.datas?.size ?: 0 > 0) {//数据为null
                            if (pageNo <= 1) {//第一页
                                loadService.showCallback(SuccessCallback::class.java)
                                it.dataBean?.data?.datas?.let { beanList ->
                                    rankListAdapter.setNewData(beanList)
                                }
                            } else {//第二三....页
                                it.dataBean?.data?.datas?.let { beanList ->
                                    rankListAdapter.addData(beanList)
                                }
                                //强制停止RecycleView的滑动
                                recyclerView?.let {
                                    CommonUtil.forceStopRecycleViewScroll(it)
                                }
                            }
                            //设置是否可以加载更多
                            if (it.dataBean?.data?.curPage!! < it.dataBean?.data?.pageCount!!) {
                                rankListAdapter.loadMoreComplete()
                                rankListAdapter.setEnableLoadMore(true)
                            } else {
                                rankListAdapter.loadMoreEnd(false)
                            }
                        } else {//数据不为null
                            if (pageNo <= 1) {//第一页,显示空布局
                                loadService.showCallback(EmptyCallback::class.java)
                            }
                        }
                    }
                    false -> {//异常处理
                        if (pageNo > 1) {
                            rankListAdapter.loadMoreFail()//上拉加载,第二页之后，加载失败时显示异常
                            pageNo--
                        } else {
                            loadService.showCallback(ErrorCallback::class.java)
                        }
                        it.error?.message?.let { it1 -> showToast(it1) }
                    }
                }
            }
        )
    }
}
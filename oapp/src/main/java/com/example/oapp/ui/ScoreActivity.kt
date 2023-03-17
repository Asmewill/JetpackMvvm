package com.example.oapp.ui

import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.adapter.ScoreAdapter

import com.example.oapp.base.BaseVmDbActivity
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.ScoreBean
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.ActivityScoreBinding
import com.example.oapp.ext.showToast
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.EventViewModel
import com.example.oapp.viewmodel.ScoreListViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.activity_point_rank_list.*
import kotlinx.android.synthetic.main.activity_point_rank_list.recyclerView
import kotlinx.android.synthetic.main.activity_score.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.*
import kotlinx.android.synthetic.main.activity_point_rank_list.swipeRefreshLayout as swipeRefreshLayout


/**
 * Created by jsxiaoshui on 2021/7/26
 */
@Route(path = Constant.PagePath.SCORE)
class ScoreActivity:BaseVmDbActivity<ScoreListViewModel,ActivityScoreBinding>() {
    private lateinit var loadService: LoadService<Any>
    private val scoreAdapter by lazy { ScoreAdapter() }
    private var pageNo: Int=1

    override fun createViewModel(): ScoreListViewModel {
        return ViewModelProvider(this).get(ScoreListViewModel::class.java)
    }

    override fun layoutId(): Int {
       return R.layout.activity_score
    }
    override fun initView() {
        rl_content.setBackgroundColor(mThemeColor)
        toolbar?.title="积分"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.let {
            it.layoutManager=LinearLayoutManager(this)
            it.adapter=scoreAdapter
        }
        //设置重新加载更多
        loadService=LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback2::class.java)
            pageNo=1
            mViewModel.getScoreList(pageNo)
        }
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener {
            pageNo=1
            mViewModel.getScoreList(pageNo)
        }
        //上拉加载更多
        scoreAdapter.setOnLoadMoreListener({
            pageNo++
            mViewModel.getScoreList(pageNo)

        },recyclerView)

    }
    override fun initData() {
        loadService.showCallback(LoadingCallback2::class.java)
        mViewModel.getScoreList(pageNo)

    }
    //处理监听
    override fun createObserver() {
        mViewModel.scoreListLiveData.observe(this,
            Observer<ListDataUiState<ScoreBean>> {
                swipeRefreshLayout?.isRefreshing=false
                when(!it.isException){
                    true->{
                        if (it.dataBean != null && it.dataBean?.data?.datas != null &&
                            it.dataBean?.data?.datas?.size ?: 0 > 0) {//数据为null
                            if (pageNo <= 1) {//第一页
                                loadService.showCallback(SuccessCallback::class.java)
                                it.dataBean?.data?.datas?.let { beanList ->
                                    scoreAdapter.setNewData(beanList)
                                }
                            } else {//第二三....页
                                it.dataBean?.data?.datas?.let { beanList ->
                                    scoreAdapter.addData(beanList)
                                }
                                //强制停止RecycleView的滑动
                                recyclerView?.let {
                                    CommonUtil.forceStopRecycleViewScroll(it)
                                }
                            }
                            //设置是否可以加载更多
                            if (it.dataBean?.data?.curPage!! < it.dataBean?.data?.pageCount!!) {
                                scoreAdapter.loadMoreComplete()
                                scoreAdapter.setEnableLoadMore(true)
                            } else {
                                scoreAdapter.loadMoreEnd(false)
                            }
                        } else {//数据不为null
                            if (pageNo <= 1) {//第一页,显示空布局
                                loadService.showCallback(EmptyCallback::class.java)
                            }
                        }
                    }
                    false->{
                        if(pageNo>1){
                            scoreAdapter.loadMoreFail()//第二页之后，加载失败时显示异常
                            pageNo--
                        }else{
                            loadService.showCallback(ErrorCallback2::class.java)
                        }
                        it.error?.message?.let { it1 -> showToast(it1) }
                    }
                }
                tv_score.text=getTotalScore(scoreAdapter).toString()
            })
    }

    fun getTotalScore(scoreAdapter:BaseQuickAdapter<ScoreBean.Data,BaseViewHolder>):Int {
       var list: MutableList<ScoreBean.Data>  =scoreAdapter.data
        var  scoreNum=0
        list.forEach {
            scoreNum+=it.coinCount
        }
        return scoreNum
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
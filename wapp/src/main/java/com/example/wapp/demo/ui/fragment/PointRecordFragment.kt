package com.example.wapp.demo.ui.fragment

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentPointRecordBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.PointRecordAdapter
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.loadcallback.ErrorCallback
import com.example.wapp.demo.viewmodel.PointRecordViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback


/**
 * Data :2022/5/26
 * Time:15:02
 * Author:shuij
 *
 */
class PointRecordFragment:BaseVmDbFragment<PointRecordViewModel,FragmentPointRecordBinding>() {
    private val pointRecordViewModel :PointRecordViewModel  by viewModels()

    private val pointRecordAdapter by lazy {
        PointRecordAdapter(this)
    }

    private var pageNo=1
    override fun layoutId(): Int {
        return R.layout.fragment_point_record
    }
    override fun initView() {
        loadService= LoadSir.getDefault().register(swipeRefresh, Callback.OnReloadListener {
            pageNo=1
            loadService.showCallback(LoadingCallback::class.java)
            pointRecordViewModel.getPointRecordList(pageNo)
        })
        toolbar.initClose(titleStr = "积分记录",onBack = {
            nav().navigateUp()
        })

        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=pointRecordAdapter
        //floatBtn快速返回到顶部
        floatbtn.setOnClickListener {
            val layoutManager:LinearLayoutManager= recyclerView.layoutManager as LinearLayoutManager
            if(layoutManager.findLastVisibleItemPosition()>=40){
                recyclerView.scrollToPosition(0)  //没有滑动动画，快速滚动到顶部
            }else{
                recyclerView.smoothScrollToPosition(0) //有滑动动画，快速滚动到顶部
            }
        }
        //滑动到顶部，隐藏 FloatingActionBtn
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(-1)){
                    floatbtn.visibility= View.INVISIBLE
                }
            }
        })
        //下拉刷新
        swipeRefresh.setOnRefreshListener {
            pageNo=1
            pointRecordViewModel.getPointRecordList(pageNo)
        }
        //添加FootView  自定义
        val footView= DefineLoadMoreView(MyApp.instance)
        recyclerView.addFooterView(footView)
        recyclerView.setLoadMoreView(footView)
        //加载更多
        recyclerView.setLoadMoreListener {
            pageNo++
            pointRecordViewModel.getPointRecordList(pageNo)
        }
        //加载失败，点击底部重新加载
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            pageNo++
            footView.onLoading()
            pointRecordViewModel.getPointRecordList(pageNo)
        })

    }
    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        pointRecordViewModel.getPointRecordList(pageNo)
    }
    override fun createObserver() {
        mViewModel.pointRecordLiveData.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing=false
             when(it.isSuccess){
                 true->{
                     if(it.response!!.getrResponseCode()==0){
                         loadService.showCallback(SuccessCallback::class.java)
                         recyclerView.loadMoreFinish(it.response!!.getResponseData().isEmpty(),it.response!!.getResponseData().curPage<it.response!!.getResponseData().pageCount)
                         if(it.response!!.getResponseData().curPage==1){
                             pointRecordAdapter.setList((it.response.getResponseData().datas))
                         }else{
                             pointRecordAdapter.addData(it.response.getResponseData().datas)
                         }
                     }else{
                         it.errorMsg?.let { errorMsg->
                             ToastUtils.showLong(errorMsg)
                         }
                     }
                 }
                 false->{
                     recyclerView.loadMoreFinish(true,false)
                     recyclerView.loadMoreError(0,it.errorMsg)
                     if(!TextUtils.isEmpty(it.errorMsg)){
                         ToastUtils.showLong(it.errorMsg)
                     }
                     if(pageNo>1){
                         pageNo--
                     }
                     if(pageNo==1){
                         loadService.showCallback(ErrorCallback::class.java)
                     }
                 }
             }
        })
    }
}
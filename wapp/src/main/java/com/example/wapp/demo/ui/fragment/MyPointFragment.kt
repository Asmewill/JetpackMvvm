package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentMyPointBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.MyPointAdapter
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.loadcallback.ErrorCallback
import com.example.wapp.demo.navigation.NavHostFragment
import com.example.wapp.demo.viewmodel.MyPointViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class MyPointFragment:BaseVmDbFragment<MyPointViewModel,FragmentMyPointBinding>() {
    private var pageNo=1;
    private val myPointViewModel :MyPointViewModel by viewModels()
    private val myPointAdapter by lazy {
        MyPointAdapter(this)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_my_point
    }
    override fun initView() {
        mDataBind.toolbar.initClose(titleStr = "我的积分",onBack={
            NavHostFragment.findNavController(this).navigateUp()
        })

        mDataBind.toolbar.inflateMenu(R.menu.integral_menu)
        mDataBind.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.integral_guize->{
                    val bundle=Bundle()
                    bundle.putString(Constant.ARTICLE_TITLE,"积分规则")
                    bundle.putString(Constant.URL,"https://www.wanandroid.com/blog/show/2653")
                    bundle.putBoolean(Constant.IS_COLLECT,false)
                    bundle.putInt(Constant.COLLECT_TYPE,CollectType.Url.type)
                    nav().navigate(R.id.action_MyPointFragment_to_WebFragment,bundle)
                }
                R.id.integral_history->{
                    nav().navigate(R.id.action_MyPointFragment_to_PointRecordFragment)
                }
            }
            true
        }
        loadService=LoadSir.getDefault().register(mDataBind.swipeRefresh, Callback.OnReloadListener {
            pageNo=1
            loadService.showCallback(LoadingCallback::class.java)
            myPointViewModel.getMyPointList(pageNo)
        })
        mDataBind.recyclerView.layoutManager=LinearLayoutManager(activity)
        mDataBind.recyclerView.adapter=myPointAdapter
        //floatBtn快速返回到顶部
        mDataBind.floatbtn.setOnClickListener {
            val layoutManager:LinearLayoutManager= mDataBind.recyclerView.layoutManager as LinearLayoutManager
            if(layoutManager.findLastVisibleItemPosition()>=40){
                mDataBind.recyclerView.scrollToPosition(0)  //没有滑动动画，快速滚动到顶部
            }else{
                mDataBind.recyclerView.smoothScrollToPosition(0) //有滑动动画，快速滚动到顶部
            }
        }
        //滑动到顶部，隐藏 FloatingActionBtn
        mDataBind.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(-1)){
                    mDataBind.floatbtn.visibility= View.INVISIBLE
                }
            }
        })
        //下拉刷新
        mDataBind.swipeRefresh.setOnRefreshListener {
            pageNo=1
            myPointViewModel.getMyPointList(pageNo)
        }
        //添加FootView  自定义
        val footView= DefineLoadMoreView(MyApp.instance)
        mDataBind.recyclerView.addFooterView(footView)
        mDataBind.recyclerView.setLoadMoreView(footView)
        //加载更多
        mDataBind.recyclerView.setLoadMoreListener {
            pageNo++
            myPointViewModel.getMyPointList(pageNo)
        }
        //加载失败，点击底部重新加载
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            pageNo++
            footView.onLoading()
            myPointViewModel.getMyPointList(pageNo)
        })

    }

    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        myPointViewModel.getMyPointList(pageNo)
    }
    override fun createObserver() {
        myPointViewModel.pointLiveData.observe(this, Observer {
            mDataBind.swipeRefresh.isRefreshing=false
            if(it.isSuccess){
                if(it.response!!.getStatus()){
                    loadService.showCallback(SuccessCallback::class.java)
                    mDataBind.recyclerView.loadMoreFinish(it.response!!.getResponseData().isEmpty(),it.response!!.getResponseData().curPage<it.response!!.getResponseData().pageCount)
                    if(it.response!!.getResponseData().curPage==1){
                        myPointAdapter.setList((it.response.getResponseData().datas))
                    }else{
                        myPointAdapter.addData(it.response.getResponseData().datas)
                    }
                }else{
                   ToastUtils.showLong(it.response.getResponseMes())
                    loadService.showCallback(ErrorCallback::class.java)
                }

            }else{
                mDataBind.recyclerView.loadMoreFinish(true,false)
                mDataBind.recyclerView.loadMoreError(0,it.errorMsg)
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
        })
    }
}
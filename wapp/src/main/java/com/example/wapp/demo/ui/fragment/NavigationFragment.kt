package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSquareListBinding
import com.example.wapp.demo.adapter.NavigatorAdapter
import com.example.wapp.demo.bean.ArticleResponse
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.viewmodel.SquareViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import com.example.wapp.demo.loadcallback.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-11-08
 */
class NavigationFragment:BaseVmDbFragment<SquareViewModel,FragmentSquareListBinding>() {
    private val navigatorAdapter by lazy {
        NavigatorAdapter()
    }
    companion object{
        fun newInstance():NavigationFragment{
            val navigationFragment=NavigationFragment()
            val bundle= Bundle()
            navigationFragment.arguments=bundle
            return navigationFragment
        }
    }
    override fun layoutId(): Int {
        return R.layout.fragment_square_list
    }

    override fun initView() {
        //注册LoadingService,并设置--重试监听
        loadService = LoadSir.getDefault().register(mDataBind.swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getNavigationData(true)
        }
        mDataBind.recyclerView.layoutManager= LinearLayoutManager(mActivity)
        mDataBind.recyclerView.setHasFixedSize(true)//Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        mDataBind. recyclerView.adapter=navigatorAdapter
        //recycle滑动到顶部时,隐藏floatbtn
        mDataBind.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(-1)) {
                    mDataBind.floatbtn.visibility = View.INVISIBLE
                }
            }
        })
        //快速返回顶部
        mDataBind.floatbtn.setOnClickListener{
            val layoutManager=mDataBind.recyclerView.layoutManager as LinearLayoutManager
            if(layoutManager.findLastVisibleItemPosition()>=40){
                mDataBind.recyclerView.scrollToPosition(0) //没有滚动动画，快速滑动到底部
            }else{
                mDataBind.recyclerView.smoothScrollToPosition(0)//有滚动动画，快速滑动到底部
            }
        }
        mDataBind.swipeRefresh.setOnRefreshListener {
            mViewModel.getNavigationData(true)
        }
        navigatorAdapter.setOnItemClickListener { adapter, view, position ->
            val item=adapter.data.get(position) as ArticleResponse
            val bundle=Bundle()
            bundle.putString(Constant.ARTICLE_TITLE,item.title)
            bundle.putString(Constant.URL,item.link)
            bundle.putInt(Constant.ARTICLE_ID,item.id)
            bundle.putInt(Constant.COLLECT_TYPE, CollectType.Article.type)
            bundle.putBoolean(Constant.IS_COLLECT,false)
            nav().navigate(R.id.action_Main_to_WebFragment,bundle)
        }
    }
    override fun initData() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getNavigationData(true)
    }

    override fun createObserver() {
        mViewModel.navigatorLiveData.observe(mActivity, Observer {
            mDataBind.swipeRefresh.isRefreshing=false
            if(it.isSuccess){
                when{
                    it.isFirstEmpty->{
                        loadService.showCallback(EmptyCallback::class.java)
                    }
                    it.isRefresh->{
                        loadService.showCallback(SuccessCallback::class.java)
                        navigatorAdapter.setList(it.listData)
                    }else->{
                     loadService.showCallback(SuccessCallback::class.java)
                     navigatorAdapter.addData(it.listData)
                }
                }
            }else{
                if(it.isRefresh){
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{
                    mDataBind.recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })

    }
}
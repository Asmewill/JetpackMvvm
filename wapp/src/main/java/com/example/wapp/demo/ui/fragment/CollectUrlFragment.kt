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
import com.example.wapp.databinding.FragmentCollectUrlBinding
import com.example.wapp.demo.adapter.CollectUrlAdapter
import com.example.wapp.demo.bean.ArticleResponse
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.loadcallback.ErrorCallback
import com.example.wapp.demo.viewmodel.CollectViewModel
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_home.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import java.lang.Exception

/**
 * Created by jsxiaoshui on 2021-11-15
 * 收藏--网址
 */
class CollectUrlFragment:BaseVmDbFragment<CollectViewModel,FragmentCollectUrlBinding>() {
    private val collectViewModel:CollectViewModel by viewModels()
    private val collectUrlAdapter by lazy {
        CollectUrlAdapter()
    }

    override fun layoutId(): Int {
       return R.layout.fragment_collect_url
    }

    override fun initView() {
        loadService= LoadSir.getDefault().register(swipeRefresh, Callback.OnReloadListener {
            loadService.showCallback(LoadingCallback::class.java)
            collectViewModel.collectUrlList();
        })
        recyclerView.layoutManager=LinearLayoutManager(mActivity)
        recyclerView.adapter=collectUrlAdapter;
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
            collectViewModel.collectUrlList();
        }
        collectUrlAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item=adapter.data.get(position) as ArticleResponse
            val bundle= Bundle()
            bundle.putString(Constant.ARTICLE_TITLE,item.title)
            bundle.putString(Constant.URL,item.link)
            bundle.putInt(Constant.ARTICLE_ID,item.id)
            bundle.putInt(Constant.COLLECT_TYPE, CollectType.Article.type)
            bundle.putBoolean(Constant.IS_COLLECT,false)
            //try-catch防止重复点击导致跳转多次
            try {
                nav().navigate(R.id.action_CollectArticleFragment_to_WebFragment,bundle)
            }catch (e: Exception){}
        }
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        collectViewModel.collectUrlList();
    }

    override fun createObserver() {
        collectViewModel.urlListLiveData.observe(this, Observer {
            swipeRefresh.isRefreshing=false
            when(it.isSuccess){
                true->{//请求成功
                    if(it.response!!.getStatus()){
                        loadService.showCallback(SuccessCallback::class.java)
                        collectUrlAdapter.setList(it.response.getResponseData())
                    }else{
                        if(!TextUtils.isEmpty(it.response.getResponseMes())){
                            ToastUtils.showLong(it.response.getResponseMes())
                        }
                    }
                }
                false->{//报错，异常
                    if(!TextUtils.isEmpty(it.errorMsg)){
                        ToastUtils.showLong(it.errorMsg)
                    }
                    loadService.showCallback(ErrorCallback::class.java)
                }
            }
        })
    }
}
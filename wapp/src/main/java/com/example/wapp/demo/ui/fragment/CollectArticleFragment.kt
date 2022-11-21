package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentCollectArticleBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.CollectArticleAdapter
import com.example.wapp.demo.bean.ArticleResponse
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.loadcallback.ErrorCallback
import com.example.wapp.demo.viewmodel.CollectViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.OnItemClickListener
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import java.lang.Exception

/**
 * Created by jsxiaoshui on 2021-11-15
 * 收藏--文章
 */
class CollectArticleFragment: BaseVmDbFragment<CollectViewModel, FragmentCollectArticleBinding>() {
    private var pageNo: Int=1
    private val collectViewModel:CollectViewModel by viewModels()
    private val collectArticleAdapter by lazy {
        CollectArticleAdapter(mutableListOf())
    }

    override fun layoutId(): Int {
       return R.layout.fragment_collect_article
    }

    override fun initView() {
        loadService= LoadSir.getDefault().register(swipeRefresh, Callback.OnReloadListener {
            pageNo=1
            loadService.showCallback(LoadingCallback::class.java)
            collectViewModel.collectArticleList(pageNo)
        })
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=collectArticleAdapter
        //floatBtn快速返回到顶部
        floatbtn.setOnClickListener {
            val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
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
            collectViewModel.collectArticleList(pageNo)
        }
        //添加FootView  自定义
        val footView= DefineLoadMoreView(MyApp.instance)
        recyclerView.addFooterView(footView)
        recyclerView.setLoadMoreView(footView)
        //加载更多
        recyclerView.setLoadMoreListener {
            pageNo++
            collectViewModel.collectArticleList(pageNo)
        }
        //加载失败，点击底部重新加载
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            pageNo++
            footView.onLoading()
            collectViewModel.collectArticleList(pageNo)
        })

        collectArticleAdapter.setOnItemClickListener { adapter, view, position ->
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
        collectViewModel.collectArticleList(pageNo)

    }

    override fun createObserver() {
        collectViewModel.articleListLiveData.observe(this, Observer {
            swipeRefresh.isRefreshing=false
            when(it.isSuccess){
                true->{
                    loadService.showCallback(SuccessCallback::class.java)
                    recyclerView.loadMoreFinish(it.response!!.getResponseData().isEmpty(),it.response!!.getResponseData().curPage<it.response!!.getResponseData().pageCount)
                    if(it.response!!.getResponseData().curPage==0){
                        collectArticleAdapter.setList(it.response.getResponseData().datas)
                    }else{
                        collectArticleAdapter.addData(it.response.getResponseData().datas)
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
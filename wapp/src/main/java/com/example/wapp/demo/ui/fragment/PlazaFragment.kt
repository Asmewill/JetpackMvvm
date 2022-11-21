package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSquareListBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.bean.ArticleResponse
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.viewmodel.SquareViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_recyclerview.recyclerView
import kotlinx.android.synthetic.main.include_recyclerview.swipeRefresh
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import com.example.wapp.demo.loadcallback.ErrorCallback
import com.yanzhenjie.recyclerview.OnItemClickListener
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-11-08
 */
class PlazaFragment:BaseVmDbFragment<SquareViewModel,FragmentSquareListBinding>() {
    private val footView by lazy {
       DefineLoadMoreView(MyApp.instance)
    }
    private val articleAdapter by lazy {
        AriticleAdapter(arrayListOf())
    }

    companion object{
        fun newInstance():PlazaFragment{
            val plazaFragment=PlazaFragment()
            val bundle=Bundle()
            plazaFragment.arguments=bundle
            return plazaFragment
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_square_list
    }



    override fun initView() {
        //注册LoadingService,并设置--重试监听
        loadService = LoadSir.getDefault().register(swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getSquareData(true)
        }
        recyclerView.layoutManager= LinearLayoutManager(mActivity)
        recyclerView.setHasFixedSize(true)//Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        recyclerView.adapter=articleAdapter
        //recycle滑动到顶部时,隐藏floatbtn
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(-1)) {
                    floatbtn.visibility = View.INVISIBLE
                }
            }
        })
        //快速返回顶部
        floatbtn.setOnClickListener{
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            if(layoutManager.findLastVisibleItemPosition()>=40){
                recyclerView.scrollToPosition(0) //没有滚动动画，快速滑动到底部
            }else{
                recyclerView.smoothScrollToPosition(0)//有滚动动画，快速滑动到底部
            }
        }

        //给recycleView添加footview
        recyclerView.addFooterView(footView)
        recyclerView.setLoadMoreView(footView)
        //设置上拉加载更多
        recyclerView.setLoadMoreListener {
            mViewModel.getSquareData(false)
        }
        //异常时,点击footView,获取更多数据
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            mViewModel.getSquareData(false)
        })
        swipeRefresh.setOnRefreshListener {
            mViewModel.getSquareData(true)
        }
        articleAdapter.setOnItemClickListener { adapter, view, position ->
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
        loadService.showCallback(LoadingCallback::class.java)
        super.lazyLoad()
        mViewModel.getSquareData(true)
    }

    override fun createObserver() {
        mViewModel.squareLiveData.observe(mActivity, Observer {
               swipeRefresh.isRefreshing=false
               recyclerView.loadMoreFinish(it.listData.isEmpty(),it.hasMore)
               if(it.isSuccess){
                   when{
                       it.isFirstEmpty->{
                           loadService.showCallback(EmptyCallback::class.java)
                       }
                       it.isRefresh->{
                           loadService.showCallback(SuccessCallback::class.java)
                           articleAdapter.setList(it.listData)
                       }
                       else->{
                           articleAdapter.addData(it.listData)
                       }
                   }
               }else{
                   if(it.isRefresh){
                       loadService.showCallback(ErrorCallback::class.java)
                   }else{
                       recyclerView.loadMoreError(0,it.errorMsg)
                   }
               }
        })


    }
}
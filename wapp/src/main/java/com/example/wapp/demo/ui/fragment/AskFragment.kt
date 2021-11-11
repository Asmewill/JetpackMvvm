package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSquareListBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.AriticleAdapter
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
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-11-08
 */
class AskFragment:BaseVmDbFragment<SquareViewModel,FragmentSquareListBinding>() {
    lateinit var loadService: LoadService<Any>
    private val footView by lazy {
        DefineLoadMoreView(MyApp.instance)
    }
    private val articleAdapter by lazy {
        AriticleAdapter(arrayListOf())
    }
    companion object{
        fun newInstance():AskFragment{
            val askFragment=AskFragment()
            val bundle= Bundle()
            askFragment.arguments=bundle
            return askFragment
        }
    }
    override fun layoutId(): Int {
       return R.layout.fragment_square_list
    }
    override fun initView() {
        //注册LoadingService,并设置--重试监听
        loadService = LoadSir.getDefault().register(swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getAskData(true)
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
            mViewModel.getAskData(false)
        }
        //异常时,点击footView,获取更多数据
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            mViewModel.getAskData(false)
        })
        swipeRefresh.setOnRefreshListener {
            mViewModel.getAskData(true)
        }
    }

    override fun initData() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getAskData(true)
    }

    override fun createObserver() {
        mViewModel.askLiveData.observe(mActivity, Observer {
            swipeRefresh.isRefreshing=false
            recyclerView.loadMoreFinish(it.listData.isEmpty(),it.hasMore)
            if(it.isSuccess){
                when{
                    it.isEmpty->{
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
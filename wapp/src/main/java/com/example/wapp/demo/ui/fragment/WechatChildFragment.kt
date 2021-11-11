package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentProjectChildBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.viewmodel.WechatViewModel
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
 * Created by jsxiaoshui on 2021-11-03
 */
class WechatChildFragment : BaseVmDbFragment<WechatViewModel, FragmentProjectChildBinding>() {
    lateinit var loadService: LoadService<Any>
    private var cid = 0
    private val articleAdapter by lazy {
       AriticleAdapter(mutableListOf())
    }
    private val footView by lazy {
        DefineLoadMoreView(MyApp.instance)
    }
    companion object {
        fun newInstance(cid: Int): WechatChildFragment {
            val instance = WechatChildFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.CID, cid)
            instance.arguments = bundle
            return instance
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_wechat_child
    }

    override fun initView() {
        arguments?.let {
            cid = it.getInt(Constant.CID, 0)
        }
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getPublicData(true,cid)
        }
        recyclerView.layoutManager=LinearLayoutManager(mActivity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=articleAdapter
        recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(-1)) {
                    floatbtn.visibility = View.INVISIBLE
                }
            }
        })
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
            mViewModel.getPublicData(false,cid)
        }
        //异常时,点击footView,获取更多数据
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            mViewModel.getPublicData(false,cid)
        })
        swipeRefresh.setOnRefreshListener {
            mViewModel.getPublicData(true,cid)
        }
    }

    override fun lazyLoad() {
        super.lazyLoad()
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getPublicData(true,cid)
    }

    override fun initData() {

    }

    override fun createObserver() {
        mViewModel.publicDataLiveData.observe(mActivity, Observer {
            swipeRefresh.isRefreshing=false
            recyclerView.loadMoreFinish(it.listData.isEmpty(),it.hasMore)
            if(it.isSuccess){
                when{
                    //第一页，没有数据，显示空布局
                    it.isEmpty->{
                        loadService.showCallback(EmptyCallback::class.java)
                    }
                    //第一页，有数据
                    it.isRefresh->{
                        articleAdapter.setList(it.listData)
                        loadService.showCallback(SuccessCallback::class.java)
                    }
                    //不是第一页
                    else ->{
                        articleAdapter.addData(it.listData)
                    }
                }
            }else{
                //如果是第一的时候，网络异常了
                if(it.isRefresh){
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{//第二页，第三页...异常
                    //上啦加载网络异常
                    recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })
    }
}
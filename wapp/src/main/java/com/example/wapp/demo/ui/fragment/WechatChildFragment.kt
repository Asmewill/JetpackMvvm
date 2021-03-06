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
        //??????LoadingService
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
                recyclerView.scrollToPosition(0) //??????????????????????????????????????????
            }else{
                recyclerView.smoothScrollToPosition(0)//???????????????????????????????????????
            }
        }

        //???recycleView??????footview
        recyclerView.addFooterView(footView)
        recyclerView.setLoadMoreView(footView)
        //????????????????????????
        recyclerView.setLoadMoreListener {
            mViewModel.getPublicData(false,cid)
        }
        //?????????,??????footView,??????????????????
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
                    //??????????????????????????????????????????
                    it.isEmpty->{
                        loadService.showCallback(EmptyCallback::class.java)
                    }
                    //?????????????????????
                    it.isRefresh->{
                        articleAdapter.setList(it.listData)
                        loadService.showCallback(SuccessCallback::class.java)
                    }
                    //???????????????
                    else ->{
                        articleAdapter.addData(it.listData)
                    }
                }
            }else{
                //??????????????????????????????????????????
                if(it.isRefresh){
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{//?????????????????????...??????
                    //????????????????????????
                    recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })
    }
}
package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSquareListBinding
import com.example.wapp.demo.adapter.SystemAdapter
import com.example.wapp.demo.viewmodel.SquareViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_recyclerview.recyclerView
import kotlinx.android.synthetic.main.include_recyclerview.swipeRefresh
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import com.example.wapp.demo.loadcallback.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-11-08
 */
class SystemFragment:BaseVmDbFragment<SquareViewModel,FragmentSquareListBinding>() {
    lateinit var loadService: LoadService<Any>

    private val systemAdapter by lazy {
        SystemAdapter(mutableListOf())
    }
    companion object{
        fun newInstance():SystemFragment{
            val systemFragment=SystemFragment()
            val bundle= Bundle()
            systemFragment.arguments=bundle
            return systemFragment
        }
    }
    override fun layoutId(): Int {
       return  R.layout.fragment_square_list
    }

    override fun initView() {
        //注册LoadingService,并设置--重试监听
        loadService = LoadSir.getDefault().register(swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getSystemData(true)
        }
        recyclerView.layoutManager= LinearLayoutManager(mActivity)
        recyclerView.setHasFixedSize(true)//Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        recyclerView.adapter=systemAdapter
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
        swipeRefresh.setOnRefreshListener {
            mViewModel.getSystemData(true)
        }

    }

    override fun initData() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getSystemData(true)
    }


    override fun createObserver() {
        mViewModel.systemLiveData.observe(mActivity, Observer {
            swipeRefresh.isRefreshing=false
            if(it.isSuccess){
                when{
                    it.isFirstEmpty->{
                        loadService.showCallback(EmptyCallback::class.java)
                    }
                    it.isRefresh->{
                        loadService.showCallback(SuccessCallback::class.java)
                        systemAdapter.setList(it.listData)
                    }else->{
                       loadService.showCallback(SuccessCallback::class.java)
                       systemAdapter.addData(it.listData)
                    }
                }
            }else{
                if(it.isRefresh){
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{
                  recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })
    }
}
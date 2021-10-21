package com.example.wapp.demo.ui.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentHomeBinding
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.adapter.HomeBannerAdapter
import com.example.wapp.demo.adapter.HomeBannerViewHolder
import com.example.wapp.demo.bean.BannerResponse
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initFloatBtn
import com.example.wapp.demo.ext.initFooter
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.viewmodel.HomeViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import java.util.*

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class HomeFragment : BaseVmDbFragment<HomeViewModel, FragmentHomeBinding>() {
    lateinit var loadService: LoadService<Any>
    private lateinit var footView: DefineLoadMoreView
    private lateinit var headerView:View
    private val articleAdapter by lazy {
        AriticleAdapter(arrayListOf(), true)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mDataBind.root.toolbar?.run {
            this.init("首页")
            this.inflateMenu(R.menu.home_menu)
            this.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        // NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.act_Main_to_SearFragment)
                        nav().navigate(R.id.act_Main_to_SearFragment)
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
        //初始化RecycleView
        recyclerView.init(LinearLayoutManager(activity), articleAdapter).let {
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                mViewModel.getHomeData(false)
            })
            it.initFloatBtn(floatbtn)
        }
        //初始化SwipeRefreshLayout
        swipeRefresh.init {
            mViewModel.getHomeData(true)
        }
        articleAdapter.run {
            this.setOnItemClickListener { adapter, view, position ->
                mViewModel.getHomeData(true)
            }
        }
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getBannerData()
            mViewModel.getHomeData(true)
        }
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getBannerData()
        mViewModel.getHomeData(true)
    }

    override fun createObserver() {
        mViewModel.bannerDataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
             if(recyclerView.headerCount==0){
                  headerView=LayoutInflater.from(context).inflate(R.layout.include_banner,null).apply {
                    this.findViewById<BannerViewPager<BannerResponse,HomeBannerViewHolder>>(R.id.banner_view).apply {
                        this.adapter=HomeBannerAdapter()
                        this.setLifecycleRegistry(lifecycle)
                        this.setOnPageClickListener {
                        }
                        create(it.listData)
                    }
                 }
                 recyclerView.addHeaderView(headerView)
                 recyclerView.scrollToPosition(0)
             }
        })
        mViewModel.homeDataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
                    if(recyclerView.headerCount>0){
                        recyclerView.removeHeaderView(headerView)
                    }
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{//第二页，第三页...异常
                    //上啦加载网络异常
                  recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })
    }

    fun testXieChengRequest(){
        GlobalScope.launch(Dispatchers.Main) {
            runCatching {
                //login是个suspend函数
                val result = apiService.getBanner()
            }.onSuccess {

            }.onFailure {

            }
        }
    }
}
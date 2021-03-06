package com.example.wapp.demo.ui.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.wapp.demo.loadcallback.ErrorCallback
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
            this.init("??????")
            this.inflateMenu(R.menu.home_menu)
            this.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        // NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.act_Main_to_SearFragment)
                        nav().navigate(R.id.action_Main_to_SearchFragment)
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
        //?????????RecycleView
        recyclerView.init(LinearLayoutManager(activity), articleAdapter).let {
             it.initFooter(SwipeRecyclerView.LoadMoreListener {
                mViewModel.getHomeData(false)
            })
            it.initFloatBtn(floatbtn)
        }
        //?????????SwipeRefreshLayout
        swipeRefresh.init {
            mViewModel.getHomeData(true)
        }
        articleAdapter.run {
            this.setOnItemClickListener { adapter, view, position ->
                mViewModel.getHomeData(true)
            }
        }
        //??????LoadingService
        loadService = LoadSir.getDefault().register(swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getBannerData()
            mViewModel.getHomeData(true)
        }
    }

    override fun lazyLoad() {
        super.lazyLoad()
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getBannerData()
        mViewModel.getHomeData(true)
    }

    override fun initData() {

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
                    if(recyclerView.headerCount>0){
                        recyclerView.removeHeaderView(headerView)
                    }
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{//?????????????????????...??????
                    //????????????????????????
                  recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })
    }

    fun testXieChengRequest(){
        GlobalScope.launch(Dispatchers.Main) {
            runCatching {
                //login??????suspend??????
                val result = apiService.getBanner()
            }.onSuccess {

            }.onFailure {

            }
        }
    }
}
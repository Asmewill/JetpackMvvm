package com.example.wapp.demo.ui.fragment

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class HomeFragment : BaseVmDbFragment<HomeViewModel, FragmentHomeBinding>() {
    private lateinit var footView: DefineLoadMoreView
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
            })
            it.initFloatBtn(floatbtn)
        }
        //初始化SwipeRefreshLayout
        swipeRefresh.init {

        }
        articleAdapter.run {
            this.setOnItemClickListener { adapter, view, position ->

            }
        }
    }

    override fun initData() {
        mViewModel.getBannerData()
        mViewModel.getHomeData(true)
    }

    override fun createObserver() {
      val method:()->Unit={

      }
        mViewModel.bannerDataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
             if(recyclerView.headerCount==0){
                 val headerView=LayoutInflater.from(context).inflate(R.layout.include_banner,null).apply {
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
    }

    private fun createData() {

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
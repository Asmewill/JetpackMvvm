package com.example.wapp.demo.ui.fragment

import android.provider.Settings
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentHomeBinding
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initFloatBtn
import com.example.wapp.demo.ext.initFooter
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.viewmodel.HomeViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
//        GlobalScope.launch(Dispatchers.Main) {
//            kotlin.runCatching {
//                //login是个suspend函数
//                val result = apiService.getBanner()
//            }.onSuccess {
//
//            }.onFailure {
//
//            }
//        }

    }

    override fun createObserver() {

    }
}
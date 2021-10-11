package com.example.wapp.demo.ui.fragment

import android.provider.Settings
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentHomeBinding
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class HomeFragment:BaseVmDbFragment<HomeViewModel,FragmentHomeBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mDataBind.root.toolbar?.run {
            this.init("首页")
            this.inflateMenu(R.menu.home_menu)
            this.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.home_search->{
                       // NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.act_Main_to_SearFragment)
                        nav().navigate(R.id.act_Main_to_SearFragment)
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
//        recyclerView.init(
//
//
//        )

    }

    override fun initData() {
        GlobalScope.launch (Dispatchers.Main){
            //login是个suspend函数
            val result = apiService.getBanner()
            print("result:"+result.toString());
        }




    }

    override fun createObserver() {

    }
}
package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentMyPointBinding
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.navigation.NavHostFragment
import com.example.wapp.demo.viewmodel.MyPointViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class MyPointFragment:BaseVmDbFragment<MyPointViewModel,FragmentMyPointBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_my_point
    }
    override fun initView() {
        toolbar.initClose(titleStr = "我的积分",onBack={
            NavHostFragment.findNavController(this).navigateUp()
        })
    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
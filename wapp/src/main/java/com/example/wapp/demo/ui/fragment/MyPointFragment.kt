package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentMyPointBinding
import com.example.wapp.demo.viewmodel.MyPointViewModel

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class MyPointFragment:BaseVmDbFragment<MyPointViewModel,FragmentMyPointBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_my_point
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
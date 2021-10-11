package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSquareBinding
import com.example.wapp.demo.viewmodel.SquareViewModel

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class SquareFragment:BaseVmDbFragment<SquareViewModel,FragmentSquareBinding>() {
    override fun layoutId(): Int {
       return R.layout.fragment_square
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
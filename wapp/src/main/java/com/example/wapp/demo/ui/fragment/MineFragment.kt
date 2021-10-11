package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentMineBinding
import com.example.wapp.demo.viewmodel.MineViewModel

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class MineFragment:BaseVmDbFragment<MineViewModel,FragmentMineBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
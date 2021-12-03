package com.example.wapp.demo.ui

import com.example.oapp.base.BaseVmDbActivity
import com.example.wapp.R
import com.example.wapp.databinding.ActivityMainBinding
import com.example.wapp.demo.viewmodel.MainViewModel

/**
 * Created by jsxiaoshui on 2021/8/18
 */
class MainActivity: BaseVmDbActivity<MainViewModel, ActivityMainBinding>() {

    override fun layoutId(): Int {
       return R.layout.activity_main
    }

    override fun initView() {


    }

    override fun createObserver() {

    }

    override fun initData() {


    }
}
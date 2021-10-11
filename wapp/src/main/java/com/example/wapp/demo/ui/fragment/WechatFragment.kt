package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentWechatBinding
import com.example.wapp.demo.viewmodel.WechatViewModel

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class WechatFragment:BaseVmDbFragment<WechatViewModel,FragmentWechatBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_wechat
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
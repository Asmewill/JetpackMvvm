package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSettingBinding
import com.example.wapp.demo.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class SettingFragment:BaseVmDbFragment<SettingViewModel,FragmentSettingBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun initView() {
        toolbar.title="设置"

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentRegisterBinding
import com.example.wapp.demo.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by jsxiaoshui on 2021-11-12
 */
class RegisterFragment:BaseVmDbFragment<LoginRegisterViewModel,FragmentRegisterBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initView() {
        toolbar.title="注册"

    }

    override fun initData() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
    }

    override fun createObserver() {

    }
}
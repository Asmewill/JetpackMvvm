package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentLoginBinding
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by jsxiaoshui on 2021-11-12
 */
class LoginFragment: BaseVmDbFragment<LoginRegisterViewModel, FragmentLoginBinding>() {
    override fun layoutId(): Int {
        return  R.layout.fragment_login
    }

    override fun initView() {
        toolbar.title="登录"
        mDataBind.viewModel=mViewModel
        mDataBind.click=MyClick()
    }

    override fun initData() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
    }

    override fun createObserver() {

    }

    inner class MyClick(){
        fun goToRegister(){
            nav().navigate(R.id.action_Login_to_RegisterFragment)

        }
    }
}
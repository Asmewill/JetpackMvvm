package com.example.wapp.demo.ui.fragment

import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentLoginBinding
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.navigation.NavHostFragment
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.viewmodel.EventViewModel
import com.example.wapp.demo.viewmodel.LoginRegisterViewModel


/**
 * Created by jsxiaoshui on 2021-11-12
 */
class LoginFragment: BaseVmDbFragment<LoginRegisterViewModel, FragmentLoginBinding>() {
    var accout=""
    var pwd=""
    override fun layoutId(): Int {
        return  R.layout.fragment_login
    }

    override fun initView() {
        mDataBind.toolbar.initClose(titleStr = "登录"){
            nav().navigateUp()
        }
        mDataBind.vm=mViewModel
        mDataBind.click=MyClick()
        mDataBind.etAccount.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()){
                    mDataBind.ivClear.visibility= View.VISIBLE
                }else{
                    mDataBind.ivClear.visibility= View.GONE
                }
            }
        })
        mDataBind.ivClear.setOnClickListener{
            mDataBind.etAccount.setText("")
        }
        mDataBind.cbPwd.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mDataBind.etPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                mDataBind.etPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            mDataBind.etPwd.setSelection(mDataBind.etPwd.text.toString().length)
        }
    }

    override fun initData() {
    }

    override fun lazyLoad() {
        super.lazyLoad()
    }

    override fun createObserver() {
        mViewModel.loginLiveData.observe(mActivity, Observer {
            if(it.isSuccess){
                CacheUtil.setUser(it.listData[0])
                CacheUtil.setIsLogin(true)
                if(accout.isNotEmpty()&&pwd.isNotEmpty()){
                    mViewModel.loginHx(accout,pwd)
                }
                EventViewModel.userInfoLiveData.value=it.listData[0]
                NavHostFragment.findNavController(this).navigateUp()
            }else{
               ToastUtils.showLong(it.errorMsg)
            }
        })
        mViewModel.loginHxLiveData.observe(mActivity, Observer {
            if(it.isSuccess){
                ToastUtils.showShort("环信登录成功："+it.listData[0].username)
            }else{
                ToastUtils.showShort("环信登录失败："+it.errorMsg)
            }
        })
    }

    inner class MyClick(){
        fun goToRegister(){
            nav().navigate(R.id.action_Login_to_RegisterFragment)
        }
        fun goToLogin(){
             accout=mDataBind.etAccount.text.toString().trim()
             pwd=mDataBind.etPwd.text.toString().trim()
            if(TextUtils.isEmpty(accout)){
                ToastUtils.showLong("请输入账号")
                return
            }
            if(TextUtils.isEmpty(pwd)){
                ToastUtils.showLong("密码")
                return
            }
            mViewModel.goLogin(accout,pwd)
        }
    }
}
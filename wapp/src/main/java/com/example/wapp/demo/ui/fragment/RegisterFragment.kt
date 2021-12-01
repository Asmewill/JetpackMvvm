package com.example.wapp.demo.ui.fragment

import android.text.InputType
import android.text.TextUtils
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentRegisterBinding
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.navigation.NavHostFragment
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.viewmodel.EventViewModel
import com.example.wapp.demo.viewmodel.LoginRegisterViewModel
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.lang.Exception
import kotlin.concurrent.thread
import com.blankj.utilcode.util.Utils.runOnUiThread as runOnUiThread1


/**
 * Created by jsxiaoshui on 2021-11-12
 */
class RegisterFragment:BaseVmDbFragment<LoginRegisterViewModel,FragmentRegisterBinding>() {
    var userName=""
    var pwd1=""
    var pwd2=""
    override fun layoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initView() {
        toolbar.initClose(titleStr = "注册",onBack={
            NavHostFragment.findNavController(this).navigateUp()
        })
        registerUsername.addTextChangedListener{
            if(it.toString().isNotEmpty()){
                registerClear.visibility= View.VISIBLE
            }else{
                registerClear.visibility=View.GONE
            }
        }
        registerClear.setOnClickListener {
            registerUsername.setText("")
        }
        registerKey.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                registerPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                registerPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            registerPwd.setSelection(registerPwd.text.toString().length)
        }
        registerKey1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                registerPwd1.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                registerPwd1.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            registerPwd.setSelection(registerPwd1.text.toString().length)
        }
        registerSub.setOnClickListener {
             userName=registerUsername.text.toString().trim()
             pwd1=registerPwd.text.toString().trim()
             pwd2=registerPwd1.text.toString().trim()
            if(TextUtils.isEmpty(userName)){
                ToastUtils.showLong("用户名不能为空")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(pwd1)||TextUtils.isEmpty(pwd2)){
                ToastUtils.showShort("密码不能为空")
                return@setOnClickListener
            }
            if(pwd1 != pwd2){
                ToastUtils.showShort("二次密码输入不一致")
                return@setOnClickListener
            }
            mViewModel.register(userName,pwd1)
        }

    }

    override fun initData() {


    }

    override fun lazyLoad() {
        super.lazyLoad()
    }

    override fun createObserver() {
        mViewModel.registerLiveData.observe(mActivity, androidx.lifecycle.Observer {
           if(it.isSuccess){
               if(userName.isNotEmpty()&&pwd1.isNotEmpty()){
                   mViewModel.goLogin(userName,pwd1)
                   //环信注册
                   mViewModel.registerHx(userName,pwd1)
               }
           }else{
               ToastUtils.showLong(it.errorMsg)
           }
        })
        mViewModel.loginLiveData.observe(mActivity, Observer {
            if(it.isSuccess){
                CacheUtil.setUser(it.listData[0])
                CacheUtil.setIsLogin(true)
                if(userName.isNotEmpty()&&pwd1.isNotEmpty()){
                    mViewModel.loginHx(userName,pwd1)
                }
                EventViewModel.userInfoLiveData.value=it.listData[0]
                nav().navigate(R.id.action_Register_to_MainFragment)
            }else{
                ToastUtils.showShort(it.errorMsg)
            }
        })
        //环信注册回掉
        mViewModel.registerHxLiveData.observe(mActivity, Observer {
            if(it.isSuccess){
                ToastUtils.showShort("环信注册成功："+it.listData[0])
            }else{
                ToastUtils.showShort("环信注册失败"+it.errorMsg)
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
}
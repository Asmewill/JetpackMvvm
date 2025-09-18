package com.example.oapp.ui

import android.content.Intent
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.base.BaseActivity
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.LoginBean
import com.example.oapp.constant.Constant
import com.example.oapp.event.LoginEvent
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.utils.DialogUtil
import com.example.oapp.utils.Preference
import com.example.oapp.utils.SettingUtil
import com.example.oapp.utils.SharedPreUtil
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find

/**
 * Created by jsxiaoshui on 2021/7/9
 */
@Route(path = Constant.PagePath.REGISTER)
class RegisterActivity:BaseActivity(),View.OnClickListener {

    lateinit var toolbar: Toolbar
    lateinit var tv_sign_in: TextView
    lateinit var btn_register: TextView

    lateinit var  et_username: EditText
    lateinit var et_password: EditText
    lateinit var et_password2: EditText

    private val loadingDialog by lazy {
        DialogUtil.getWaitDialog(this,"注册中...")
    }

    override fun attachLayoutRes(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        toolbar=findViewById<Toolbar>(R.id.toolbar)
        tv_sign_in=findViewById<TextView>(R.id.tv_sign_in)
        btn_register=findViewById<TextView>(R.id.btn_register)
        et_username=findViewById<EditText>(R.id.et_username)
        et_password=findViewById<EditText>(R.id.et_password)
        et_password2=findViewById<EditText>(R.id.et_password2)

        toolbar?.let {
            it.title="注册"
            setSupportActionBar(it)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tv_sign_in.setOnClickListener(this)
        btn_register.setOnClickListener(this)
    }
    override fun initColor() {
        super.initColor()
        btn_register.setBackgroundColor(mThemeColor)
    }

    override fun initData() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private var username:String by Preference(Constant.USER_NAME_KEY,"")
    private var pwd:String by Preference(Constant.PASSWORD_KEY,"")
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_sign_in->{
                ARouter.getInstance().build(Constant.PagePath.REGISTER).navigation()
                finish()
            }
            R.id.btn_register->{
                val userNameStr=et_username.text.toString()
                val pwdStr1=et_password.text.toString()
                val pwdStr2=et_password2.text.toString()
                if(TextUtils.isEmpty(userNameStr)){
                    showToast("用户名不能为空")
                    return
                }
                if(TextUtils.isEmpty(pwdStr1)||TextUtils.isEmpty(pwdStr2)){
                    showToast("密码不能为空")
                    return
                }
                if(!pwdStr1.equals(pwdStr2)){
                    showToast("二次密码输入不一致")
                    return
                }
                loadingDialog.show()
                HttpRetrofit.apiService.register(userNameStr,pwdStr1,pwdStr2).applySchdules().subscribe(
                    OObserver(object:ApiCallback<HttpResult<LoginBean>>{
                    override fun onSuccess(t: HttpResult<LoginBean>) {
                        if(t.errorCode==-1){
                            showToast(t.errorMsg)
                        }else{
                            SharedPreUtil.saveString(this@RegisterActivity,Constant.LOGIN_BEAN,Gson().toJson(t.data))
                            t.data?:return
                            isLogin=true
                            username=t.data?.username?:"null"
                            pwd=t.data?.password?:"null"
                            EventBus.getDefault().post(LoginEvent(true))
                            finish()
                        }
                        loadingDialog.dismiss()
                    }
                    override fun onFailture(t: Throwable) {
                        loadingDialog.dismiss()
                    }
                }))

            }
        }
    }
}

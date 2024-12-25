package com.example.oapp.base
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


/**
 * Created by jsxiaoshui on 2021/7/22
 */
abstract class BaseVmDbActivity<VM :BaseViewModel,DB: ViewDataBinding> :BaseVmActivity<VM>(){
    //该类绑定的ViewDataBinding
    lateinit var mDataBind: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initDataBind() {
        mDataBind= DataBindingUtil.setContentView(this,layoutId())
        mDataBind.lifecycleOwner=this
    }
    override fun isUseDB(): Boolean {
        return true
    }
}
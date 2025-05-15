package com.example.oapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wapp.demo.ext.getVmClazz

/**
 * Created by jsxiaoshui on 2021/7/22
 */
abstract class BaseVmActivity<VM : BaseViewModel>:AppCompatActivity() {

    lateinit var mViewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!isUseDB()){
            setContentView(layoutId())
        }else{
            initDataBind()
        }
        mViewModel=createViewModel()
        registerUiChange()
        initView()
        createObserver()//初始化View之后，在createObserver比较合理一些
        initData()
    }

    open fun createViewModel():VM{
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.showLoadingLiveData.observeInActivity(this, Observer {
           //showLoading(it)
        })
        //关闭弹窗
        mViewModel.dismissDialogLiveData.observeInActivity(this, Observer {
           dismissLoading()
        })
    }

    private fun dismissLoading() {
       // loadingDialog?.dismiss()
    }
    //设置是否使用DataBinding
   open fun isUseDB():Boolean{
        return  false
    }

    //abstract  fun createViewModel():VM
    abstract fun layoutId(): Int
    abstract  fun initView()
    abstract  fun createObserver()
    abstract  fun initData()

    //如果使用DataBinding,供子类重写
    open fun initDataBind() {
    }

}
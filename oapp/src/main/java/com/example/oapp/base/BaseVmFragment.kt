package com.example.oapp.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.oapp.R
import com.example.oapp.utils.DialogUtil
import com.example.oapp.utils.SettingUtil

/**
 * Created by jsxiaoshui on 2021/7/22
 */
abstract class BaseVmFragment<VM:BaseViewModel>:Fragment() {
     var mThemeColor: Int=SettingUtil.getColor()
    lateinit var mViewModel:VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(layoutId(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initColor()
        mViewModel=createViewModel()
        createObserver()
        registerUiChange()
        initView()
        initData()
    }


    abstract  fun layoutId():Int
    abstract fun createViewModel(): VM
    abstract fun initView()
    abstract fun initData()
    abstract fun createObserver()

    /***
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingDialog.showLoading.observeInFragment(this, Observer {
            showLoading(it)
        })
        //关闭弹窗
        mViewModel.loadingDialog.dismissDialog.observeInFragment(this, Observer {
            dismissLoading()
        })
    }

    /**
     * 将非该Fragment绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.loadingDialog.showLoading.observeInFragment(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingDialog.dismissDialog.observeInFragment(this, Observer {
                dismissLoading()
            })
        }
    }

    private fun dismissLoading() {
        loadingDialog?.let {
            it.dismiss()
        }
    }

    private var loadingDialog: ProgressDialog? = null
    /**
     * 打开等待框
     */
    fun showLoading(message: String = "loading") {
        activity?.let {
            if(!it.isFinishing){
                loadingDialog= DialogUtil.getWaitDialog(it,message)
                loadingDialog?.show()
            }
        }
    }
    /***
     * 只有Open的方法才可以被重写
     */
    open fun initColor() {
        mThemeColor=if(!SettingUtil.getIsNightMode()){
            SettingUtil.getColor()
        }else{
            resources.getColor(R.color.colorPrimary)
        }

    }


}
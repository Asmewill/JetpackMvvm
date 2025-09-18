package com.example.oapp.base

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.oapp.R
import com.example.oapp.utils.CommonUtil
import com.example.oapp.utils.DialogUtil
import com.example.oapp.utils.SettingUtil
import com.example.oapp.utils.StatusBarUtil

/**
 * Created by jsxiaoshui on 2021/6/25
 */
abstract class BaseFragment:Fragment() {
     var mThemeColor: Int=SettingUtil.getColor()
    private var loadingDialog: ProgressDialog? = null
    lateinit var mContentView:View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContentView=inflater.inflate(attachlayoutRes(),null);
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initColor()
        initView()
        initData()
    }


   abstract  fun attachlayoutRes():Int
   abstract  fun initView()
   abstract  fun initData()

    /**
     * 将非该Fragment绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.showLoadingLiveData.observeInFragment(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.dismissDialogLiveData.observeInFragment(this, Observer {
                dismissLoading()
            })
        }
    }

    private fun dismissLoading() {
        loadingDialog?.let {
            it.dismiss()
        }
    }


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
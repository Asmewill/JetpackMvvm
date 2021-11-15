package com.example.oapp.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wapp.demo.ext.getVmClazz

/**
 * Created by jsxiaoshui on 2021/7/22
 */
abstract class BaseVmFragment<VM:BaseViewModel>:Fragment() {
    private var isFirst: Boolean=true;
    lateinit var mViewModel:VM
    lateinit var mActivity: AppCompatActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity=context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(layoutId(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel=createViewModel()
        registerUiChange()
        initView()
        createObserver()
        initData()
    }

    override fun onResume() {
        super.onResume()
         if(lifecycle.currentState==Lifecycle.State.STARTED&&isFirst){
             Handler().postDelayed({
                 lazyLoad()
                 isFirst=false
             },300)
         }
    }


    abstract  fun layoutId():Int
    abstract fun initView()
    abstract fun initData()
    abstract fun createObserver()
    abstract  fun lazyLoad()
    /**
     * 创建viewModel
     */
    open fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /***
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingDialog.showLoading.observeInFragment(this, Observer {
            //showLoading(it)
        })
        //关闭弹窗
        mViewModel.loadingDialog.dismissDialog.observeInFragment(this, Observer {
           // dismissLoading()
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
                //showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingDialog.dismissDialog.observeInFragment(this, Observer {
                //dismissLoading()
            })
        }
    }

//    private fun dismissLoading() {
//        loadingDialog?.let {
//            it.dismiss()
//        }
//    }
//
//    private var loadingDialog: ProgressDialog? = null
//    /**
//     * 打开等待框
//     */
//    fun showLoading(message: String = "loading") {
//        activity?.let {
//            if(!it.isFinishing){
//                loadingDialog= DialogUtil.getWaitDialog(it,message)
//                loadingDialog?.show()
//            }
//        }
//    }



}
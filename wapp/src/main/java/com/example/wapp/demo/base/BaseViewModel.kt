package com.example.oapp.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapp.demo.bean.ApiResponse
import com.example.wapp.demo.bean.BaseResponse
import com.example.wapp.demo.bean.exception.AppException
import com.example.wapp.demo.utils.ExceptionHandle
import com.hyphenate.chat.EMClient
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.*

/**
 * Created by jsxiaoshui on 2021/7/22
 */
open class BaseViewModel : ViewModel() {
    val loadingDialog by lazy { UILoading() }

    inner class UILoading {
        val showLoading by lazy { UnPeekLiveData<String>() }
        val dismissDialog by lazy { UnPeekLiveData<String>() }
    }

    fun <T> request(
        block: suspend () -> ApiResponse<T>,
        success: (BaseResponse<T>) -> Unit,
        error: (AppException) -> Unit = {},
        isShowLoading: Boolean = false,
        loadingMsg: String = "加载中..."
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if(isShowLoading){
                    loadingDialog.showLoading.value=loadingMsg
                }
                block()
            }.onSuccess {
                success(it)
                loadingDialog.dismissDialog.value="close"
            }.onFailure {
                error(ExceptionHandle.handleException(it))
                loadingDialog.dismissDialog.value="close"
            }
        }
    }

    /**
     * 调用失败
     */
    fun  requestHX(
        block: suspend () -> Unit={},
        success: () -> Unit={},
        error: (Throwable) -> Unit = {},
        isShowLoading: Boolean = false,
        loadingMsg: String = "加载中..."
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if(isShowLoading){
                    loadingDialog.showLoading.value=loadingMsg
                }
                withContext(Dispatchers.IO) {
                    block()
                }
            }.onSuccess {
                success()
                loadingDialog.dismissDialog.value="close"
            }.onFailure {
                error(it)
                loadingDialog.dismissDialog.value="close"
            }
        }
    }


    fun <T> launch(
        block:()->T,
        success:(T)->Unit,
        error:(Throwable)->Unit={}
    ){
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO){
                    block()
                }
            }.onSuccess {
                success(it)
            }.onFailure {
                error(it)
            }
        }
    }


    open fun isLoggedIn(): Boolean {
        return EMClient.getInstance().isLoggedInBefore
    }
}
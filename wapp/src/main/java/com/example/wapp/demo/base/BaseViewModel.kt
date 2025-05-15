package com.example.oapp.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapp.demo.bean.ApiResponse
import com.example.wapp.demo.bean.BannerResponse
import com.example.wapp.demo.bean.BaseResponse
import com.example.wapp.demo.bean.exception.AppException
import com.example.wapp.demo.ui.fragment.LoginFragment
import com.example.wapp.demo.utils.ExceptionHandle
import com.hyphenate.chat.EMClient

import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.*

/**
 * Created by jsxiaoshui on 2021/7/22
 */
open class BaseViewModel : ViewModel() {
    val showLoadingLiveData by lazy { UnPeekLiveData<String>() }
    val dismissDialogLiveData by lazy { UnPeekLiveData<String>() }

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
                    showLoadingLiveData.value=loadingMsg
                }
                //接口定义，Retrofit从2.6.0版本开始支持协程*/
                //我们使用 Retrofit 请求网络，我们标记 suspend 之后再协程里面直接使用，它就是异步的
                 //其实 Retrofit 是比较特殊的情况，他的 ApiService 虽然标记了 suspend ,看起来我们是直接使用了，
                // 但是其实内部 Retrofit 的动态代理的时候会找到你是否标记了 suspend ，然后它会对 suspend 做单独的处理 。
                // 我不太会讲源码，大家如果有兴趣可以全局搜索一下 SuspendForResponse 类 和 awaitResponse 类，
                // 看看 Retrofit 怎么把 suspend 转换为协程处理的,这里我就不贴 Retrofit 的源码了。

                block()
            }.onSuccess {
                success(it)
                dismissDialogLiveData.value="close"
            }.onFailure {
                error(ExceptionHandle.handleException(it))
                dismissDialogLiveData.value="close"
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
                    dismissDialogLiveData.value=loadingMsg
                }
                withContext(Dispatchers.IO) {
                    block()
                }
            }.onSuccess {
                success()
                dismissDialogLiveData.value="close"
            }.onFailure {
                error(it)
                dismissDialogLiveData.value="close"
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
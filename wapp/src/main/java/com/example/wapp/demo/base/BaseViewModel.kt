package com.example.oapp.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapp.demo.bean.BaseResponse
import com.example.wapp.demo.bean.exception.AppException
import com.example.wapp.demo.utils.ExceptionHandle
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Appendable

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
        block: suspend () -> BaseResponse<T>,
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

    suspend fun <T> executeResponse(
        response: BaseResponse<T>,
        success: CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            when {
                response.getStatus() -> {
                    success(response.getResponseData())
                    //success(response.getResponseData())
                }
                else -> {
                    throw AppException(
                        response.getrResponseCode(),
                        response.getResponseMes(),
                        response.getResponseMes()
                    )
                }
            }
        }
    }


}
package com.example.oapp.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oapp.bean.HttpResult
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Created by Owen on 2025/4/19
 */
open class BaseViewModel: ViewModel() {
    // private val _state = MutableStateFlow("")
    val showLoadingLiveData by lazy { UnPeekLiveData<String>() }
    val dismissDialogLiveData by lazy { UnPeekLiveData<String>() }

    fun <T:Any> request(
        block: suspend () -> HttpResult<T>,
        success: (HttpResult<T>) -> Unit,
        error: (Throwable) -> Unit={},
        isShowDialog: Boolean = false,
        loadingMessage: String = "loading..."
    ): Job {
        //如果需要弹窗 通知Activity/fragment弹窗
        return viewModelScope.launch {
            runCatching  {
                if(isShowDialog){
                    showLoadingLiveData.value=loadingMessage
                }
                block()
            }.onSuccess {
                dismissDialogLiveData.value="close"
                success(it)
            }.onFailure {
                dismissDialogLiveData.value="close"
                error(it)
            }
        }
    }


}
package com.example.oapp.base

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * Created by jsxiaoshui on 2021/7/22
 */
open class BaseViewModel:ViewModel() {


     val loadingDialog by lazy { UILoading() }

     inner class UILoading{
         val showLoading by lazy { UnPeekLiveData<String>() }
         val dismissDialog by lazy { UnPeekLiveData<String>() }
     }

}
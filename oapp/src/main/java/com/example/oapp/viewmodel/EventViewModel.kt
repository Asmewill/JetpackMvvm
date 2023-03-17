package com.example.oapp.viewmodel

import com.example.oapp.base.BaseViewModel
import com.example.oapp.event.ThemeEvent
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 *
 * Created by jsxiaoshui on 2021/7/28
 *
 * fragment与fragment ,fragment与activity之间通信的ViewModel
 */
object EventViewModel:BaseViewModel() {
    var freshLiveData= UnPeekLiveData<Int>()
    val themeColorLiveData=UnPeekLiveData<ThemeEvent>()
    val showTopArticleLiveData=UnPeekLiveData<Int>()
    val noPhotoLiveData=UnPeekLiveData<Int>()
}
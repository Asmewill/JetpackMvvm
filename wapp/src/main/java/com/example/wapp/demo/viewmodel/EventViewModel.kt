package com.example.wapp.demo.viewmodel

import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.UserInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 *
 * Created by jsxiaoshui on 2021/7/28
 *
 * fragment与fragment ,fragment与activity之间通信的ViewModel
 */
object EventViewModel:BaseViewModel() {
    var userInfoLiveData= UnPeekLiveData<UserInfo>()

}
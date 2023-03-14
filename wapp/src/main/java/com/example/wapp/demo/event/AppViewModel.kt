package com.example.wapp.demo.event

import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.bean.UserInfo
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.utils.SettingUtil
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * Data :2023/2/13
 * Time:17:31
 * Author:shuij
 *
 */
class AppViewModel:BaseViewModel() {
    //App的账户信息
    var userInfo=UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()
    //App主题颜色
    var appColor=UnPeekLiveData<Int>()
    //App列表动画
    var appAnimation=UnPeekLiveData<Int>()
    init {
        //默认值保存的账户信息，没有登录过则为null
        userInfo.value=CacheUtil.getUser()
        //默认值颜色
        appColor.value=SettingUtil.getColor(MyApp.instance)
        //初始化列表动画
        appAnimation.value=SettingUtil.getListMode()
    }

}
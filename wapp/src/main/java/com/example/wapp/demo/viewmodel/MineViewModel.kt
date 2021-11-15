package com.example.wapp.demo.viewmodel

import androidx.databinding.ObservableField
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.utils.ColorUtil
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class MineViewModel:BaseViewModel() {
    var name=ObservableField<String>("请先登录~")

    var point=ObservableField<Int>(0)

    var info=ObservableField<String>("id:-- 排名:--")

    var imageUrl=ObservableField<String>(ColorUtil.randomImage())

    var testString = UnPeekLiveData<String>()


}
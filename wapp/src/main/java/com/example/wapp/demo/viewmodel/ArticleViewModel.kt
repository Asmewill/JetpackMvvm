package com.example.wapp.demo.viewmodel

import android.util.ArrayMap
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.databind.StringObservableField
import java.util.Objects

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class ArticleViewModel:BaseViewModel() {

    var shareTitle =StringObservableField()

    var shareUrl=StringObservableField()

    var shareName=StringObservableField()

    var a=ArrayMap<Any,Any>()

}
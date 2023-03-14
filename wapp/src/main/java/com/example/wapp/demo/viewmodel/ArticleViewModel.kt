package com.example.wapp.demo.viewmodel

import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.databind.StringObservableField

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class ArticleViewModel:BaseViewModel() {

    var shareTitle =StringObservableField()

    var shareUrl=StringObservableField()

    var shareName=StringObservableField()

}
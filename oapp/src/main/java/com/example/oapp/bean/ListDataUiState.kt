package com.example.oapp.bean

import java.io.Serializable

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 列表数据状态类
 */
data class ListDataUiState<T>(
    //是否请求成功
    val isException: Boolean=false,
    val error:Throwable?=null,
    var dataBean: HttpResult<T>?=null,
    //分页用到
    var pageNo:Int=0
):Serializable
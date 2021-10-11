package com.example.wapp.demo.bean

import java.io.Serializable

/**
 * Created by jsxiaoshui on 2021-10-11
 */
data class ApiPagerResponse<T>(
    var data:T,
    var curPage:Int,
    var offset:Int,
    var over:Boolean,
    var pageCount:Int,
    var size:Int,
    var total:Int
):Serializable {
    /**
     * 是否为空
     */
     fun isEmpty():Boolean {
         return (data as List<*>).size==0
     }

    /**
     * 是否下拉刷新
     */
    fun isRefresh()=offset==0

    /**
     * 是否还有更多
     */
    fun hasMore()=!over

}
package com.example.wapp.demo.bean

/**
 * Created by jsxiaoshui on 2021-10-13
 */
data class ListDataUiState<T>(
    //请求是否成功
    val isSuccess:Boolean,
    //错误提示信息
    val errorMsg:String="",
    //是否刷新
    val isRefresh:Boolean=false,
    //是否为空
    val isEmpty:Boolean =false,
    //是否有下一页
    val hasMore:Boolean=false,
    //是第一页且没有数据
    val isFirstEmpty:Boolean=false,
    //列表数据
    val listData:ArrayList<T> = arrayListOf(),
    //直接作为所有值
    val response: BaseResponse<T> ?=null
)
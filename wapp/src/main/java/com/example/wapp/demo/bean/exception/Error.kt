package com.example.wapp.demo.bean.exception

/**
 * Created by jsxiaoshui on 2021-10-13
 */
enum class Error(private val code:Int ,private val err:String) {

    /**
     * 位置错误
     */
    UNKNOW(1000,"请求失败,请稍后重试"),
    /**
     *解析错误
     */
    PARSE_ERROR(1001,"请稍后重试"),
    /***
     * 网络错误
     */
    NET_ERROR(1002,"网络连接错误，请稍后重试"),
    /***
     * 证书出错
     */
    SSL_ERROR(1003,"证书出错,请稍后重试"),
    /***
     * 连接超时
     */
    TIME_OUT(1004,"网络连接超时,请稍后重试");

    fun getValue():String{
        return err
    }

    fun getKey():Int{
        return code
    }
}
package com.example.oapp.http

import android.text.TextUtils
import com.example.oapp.MyApp
import com.example.oapp.constant.HttpConstant
import com.example.oapp.utils.SharedPreUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by jsxiaoshui on 2021/7/9
 */
class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val cookie=SharedPreUtil.getString(MyApp.instance,HttpConstant.COOKIE_KEY,"")
        val builder=chain.request().newBuilder()
        //request请求中，添加cookie
        if(!TextUtils.isEmpty(cookie)){
            builder.addHeader("Content-type", "application/json; charset=utf-8")
            builder.addHeader(HttpConstant.COOKIE_NAME,cookie)
        }
        return  chain.proceed(builder.build())
    }
}
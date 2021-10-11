package com.example.wapp.demo.http

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by jsxiaoshui on 2021-10-11
 */
class MyHeadInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder=chain.request().newBuilder()
        builder.addHeader("token", "token123456").build()
        builder.addHeader("device", "Android").build()
        builder.addHeader("isLogin", "false").build()
        return  chain.proceed(builder.build())
    }
}
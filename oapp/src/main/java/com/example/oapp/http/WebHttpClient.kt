package com.example.oapp.http


import com.example.oapp.constant.HttpConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


import java.util.concurrent.TimeUnit

/**
 * Created by jsxiaoshui on 2021/6/28
 */
object WebHttpClient {

    fun getOkHttpClinet():OkHttpClient{
        var httpLoggingInterceptor=HttpLoggingInterceptor()
//        if(BuildConfig.DEBUG){
//            httpLoggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
//        }else{
//            httpLoggingInterceptor.level=HttpLoggingInterceptor.Level.NONE
//        }
        httpLoggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        var okhttpClient=OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(CookieInterceptor())//保存cookie
            .addInterceptor(HeaderInterceptor())//添加cookie
            .connectTimeout(HttpConstant.DEFAULT_TIME_OUT,TimeUnit.SECONDS)
            .writeTimeout(HttpConstant.DEFAULT_TIME_OUT,TimeUnit.SECONDS)
            .readTimeout(HttpConstant.DEFAULT_TIME_OUT,TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        return okhttpClient
    }
}
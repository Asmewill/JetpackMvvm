package com.example.oapp.http

import com.example.oapp.constant.Constant
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jsxiaoshui on 2021/6/28
 */
object HttpRetrofit {
    val apiService by lazy {
        getRetrofit().create(ApiService::class.java);
    }
    private  var retrofit: Retrofit?=null
    private fun getRetrofit():Retrofit{
        if(retrofit==null){
            synchronized(HttpRetrofit::class.java){
                if(retrofit==null){
                    retrofit=Retrofit.Builder().baseUrl(Constant.BASE_URL).client(WebHttpClient.getOkHttpClinet())
                        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
                }
            }
        }
        return retrofit!!
    }

}
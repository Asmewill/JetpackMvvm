package com.example.wapp.demo.http

import com.example.wapp.demo.MyApp
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by jsxiaoshui on 2021-10-11
 */
val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.instance.getApi(ApiService::class.java, ApiService.SERVER_URL)
}

class NetworkApi : BaseNetworkApi() {
    companion object {
        val instance: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }


    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            this.cache(Cache(File(MyApp.instance.cacheDir,"http_cache"),10 * 1024 * 1024))
            //添加cookie自动持久化
            this.cookieJar(cookieJar)
            //添加请求头
            this.addInterceptor(MyHeadInterceptor())
            //超时时间 连接、读、写
            this.connectTimeout(10, TimeUnit.SECONDS)
            this.readTimeout(5, TimeUnit.SECONDS)
            this.writeTimeout(5, TimeUnit.SECONDS)

        }
        return  builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

    val cookieJar:PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MyApp.instance))
    }

}
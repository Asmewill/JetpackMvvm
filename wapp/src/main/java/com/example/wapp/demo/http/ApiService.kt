package com.example.wapp.demo.http

import com.example.wapp.demo.bean.ApiPagerResponse
import com.example.wapp.demo.bean.ApiResponse
import com.example.wapp.demo.bean.AriticleResponse
import com.example.wapp.demo.bean.BannerResponse
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by jsxiaoshui on 2021-10-11
 */
interface ApiService {
   //URL	https://wanandroid.com/banner/json
    companion object{
        const val  SERVER_URL="https://wanandroid.com"
    }

    /***
     * 获取banner数据
     */
    @GET("banner/json")
    suspend fun getBanner():ApiResponse<ArrayList<BannerResponse>>

    /**
     * 获取置顶数据
     */
    @GET("article/top/json")
    suspend fun getTopArticleList():ApiResponse<ArrayList<AriticleResponse>>
    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") pageNo:Int):ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>






}
package com.example.wapp.demo.http

import com.example.wapp.demo.bean.*
import retrofit2.http.*

/**
 * Created by jsxiaoshui on 2021-10-11
 */
interface ApiService {
    //URL	https://wanandroid.com/banner/json
    companion object {
        const val SERVER_URL = "https://wanandroid.com"
    }

    /***
     * 获取banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerResponse>>

    /**
     * 获取置顶数据
     */
    @GET("article/top/json")
    suspend fun getTopArticleList(): ApiResponse<ArrayList<AriticleResponse>>

    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("hotkey/json")
    suspend fun getSearchData(): ApiResponse<ArrayList<SearchResponse>>

    @POST("article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 项目分类标题
     */
    @GET("project/tree/json")
    suspend fun getProjectTitle(

    ): ApiResponse<ArrayList<ClassifyResponse>>

    /**
     * 根据分类id获取项目数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectDataByType(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 获取最新项目数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjectNewData(
        @Path("page") pageNo: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

}
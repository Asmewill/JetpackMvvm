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
    suspend fun getTopArticleList(): ApiResponse<ArrayList<ArticleResponse>>

    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    @GET("hotkey/json")
    suspend fun getSearchData(): ApiResponse<ArrayList<SearchResponse>>

    @POST("article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

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
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    /**
     * 获取最新项目数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjectNewData(
        @Path("page") pageNo: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    /**
     * 广场列表数据
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    /**
     * 每日一问列表数据
     */
    @GET("wenda/list/{page}/json")
    suspend fun getAskData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getSystemData(): ApiResponse<ArrayList<SystemResponse>>


    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationData(): ApiResponse<ArrayList<NavigationResponse>>

    /**
     * 知识体系下的文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getSystemChildData(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    /**
     * 公众号分类
     */
    @GET("wxarticle/chapters/json")
    suspend fun getPublicTitle(): ApiResponse<ArrayList<ClassifyResponse>>

    /**
     * 获取公众号数据
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getPublicData(
        @Path("page") pageNo: Int,
        @Path("id") id: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>


    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): ApiResponse<UserInfo>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String, @Field("password") pwd: String, @Field(
            "repassword"
        ) rpwd: String
    ): ApiResponse<Any>

    /**
     * 获取当前账户的个人积分和排名
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getPointAndRank(): ApiResponse<PointBean>

    /***
     * 积分排行
     */
    @GET("coin/rank/{page}/json")
    suspend fun getPointList(@Path("page") pageNo:Int): ApiResponse<ApiPagerResponse<ArrayList<Point>>>

    /***
     * 积分记录
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun getPointRecord(@Path("page") pageNo:Int): ApiResponse<ApiPagerResponse<ArrayList<PointRecordBean>>>



    /**
     * 收藏文章
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 取消收藏文章
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 收藏网址
     */
    @POST("lg/collect/addtool/json")
    suspend fun collectUrl(
        @Query("name") name: String,
        @Query("link") link: String
    ): ApiResponse<CollectUrlResponse>


    /**
     * 收藏文章列表
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun  collectArticleList(@Path("page") pageNo:Int):ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>


    /**
     * 收藏文章列表
     */
    @GET("lg/collect/usertools/json")
    suspend fun  collectUrlList():ApiResponse<ArrayList<UrlBean>>




}
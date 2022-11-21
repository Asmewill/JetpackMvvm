package com.example.wapp.demo.http

import com.example.wapp.demo.bean.ApiPagerResponse
import com.example.wapp.demo.bean.ApiResponse
import com.example.wapp.demo.bean.ArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Created by jsxiaoshui on 2021-10-13
 */

val httpRequestManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
    HttpRequestManager()
}

class HttpRequestManager {
    /***
     * 二个异步请求合并成一个
     */
    suspend fun getHomeData(pageNo:Int):ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>{
       return withContext(Dispatchers.IO){
            val listData=async {
                apiService.getArticleList(pageNo)
            }
            if(pageNo==0){
                val topData= async {apiService.getTopArticleList()  }
                //标识是否是置顶数据
                topData.await().data.forEach {
                    it.isTop=true
                }
                listData.await().data.datas.addAll(0,topData.await().data)
                listData.await() //最后一行作为返回值
            }else{
                listData.await() //最后一行作为返回值
            }
        }
    }

    suspend fun getProjectData(
        pageNo:Int,
        cid:Int=0,
        isNew:Boolean
    ):ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>{
        return if(isNew){
            apiService.getProjectNewData(pageNo)
        }else{
            apiService.getProjectDataByType(pageNo,cid)
        }
    }
}
package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.*
import com.example.wapp.demo.http.apiService

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class CollectViewModel:BaseViewModel() {
    val collectLiveData=MutableLiveData<ListDataUiState<Any?>>()
    val unCollectLiveData=MutableLiveData<ListDataUiState<Any?>>()
    val collectUrlLiveData=MutableLiveData<ListDataUiState<CollectUrlResponse>>()
    val articleListLiveData=MutableLiveData<ListDataUiState<ApiPagerResponse<ArrayList<ArticleResponse>>>>()
    val urlListLiveData=MutableLiveData<ListDataUiState<ArrayList<UrlBean>>>()

    fun collectArticle(articleId:Int){
        request(
            block = {
                    apiService.collect(articleId)
            },
            success = {
                     val listDataUiState=ListDataUiState<Any?>(
                         isSuccess = true,
                         response = it
                     )
                collectLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<Any?>(
                    isSuccess = false,
                    errorMsg = it.errorMsg
                )
                collectLiveData.value=listDataUiState
            }
        )
    }

    fun unCollectArticle(articleId:Int){
        request(
            block = {
                apiService.uncollect(articleId)
            },
            success = {
                      val listDataUiState=ListDataUiState<Any?>(
                          isSuccess = true,
                          response = it
                      )
                unCollectLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<Any?>(
                    isSuccess = false,
                    errorMsg = it.errorMsg
                )
                unCollectLiveData.value=listDataUiState
            }
        )
    }

    fun collectUrl(title:String,link:String){
        request(
            block = {
                apiService.collectUrl(title,link)
            },
            success = {
               val listDataUiState=ListDataUiState<CollectUrlResponse>(
                   isSuccess = true,
                   response = it
               )
                collectUrlLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<CollectUrlResponse>(
                    isSuccess = false,
                    errorMsg = it.errorMsg
                )
                collectUrlLiveData.value=listDataUiState
            }
        )
    }

    fun collectArticleList(pageNo:Int){
        request(
            block = {
                apiService.collectArticleList(pageNo)
            },
            success = {
                val listDataUiState=ListDataUiState<ApiPagerResponse<ArrayList<ArticleResponse>>>(
                    isSuccess = true,
                    response = it
                )
                articleListLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ApiPagerResponse<ArrayList<ArticleResponse>>>(
                    isSuccess = false,
                    errorMsg = it.errorMsg
                )
                articleListLiveData.value=listDataUiState
            }
        )

    }

    fun collectUrlList(){
        request(
            block = {
                apiService.collectUrlList()
            },
            success = {
                val listDataUiState=ListDataUiState<ArrayList<UrlBean>>(
                    isSuccess = true,
                    response = it
                )
                urlListLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ArrayList<UrlBean>>(
                    isSuccess = false,
                    errorMsg = it.errorMsg
                )
                urlListLiveData.value=listDataUiState
            }
        )

    }



}
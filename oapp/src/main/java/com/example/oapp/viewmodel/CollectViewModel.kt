package com.example.oapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.oapp.bean.CollectArticle
import com.example.oapp.bean.CollectDataUiState
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.http.HttpRetrofit
import java.text.FieldPosition

/**
 * Created by jsxiaoshui on 2021/7/27
 */
class CollectViewModel:BaseViewModel() {

     val addCollectLiveData=MutableLiveData<CollectDataUiState<Any>>()
     val cancelCollectLiveData=MutableLiveData<CollectDataUiState<Any>>()
     val collectLiveData=MutableLiveData<ListDataUiState<CollectArticle>>()

    fun getCollectList(pageNo:Int){
        request(
            block = { HttpRetrofit.apiService.getCollectList(pageNo)},
            success = {
                      val listDataUiState=ListDataUiState<CollectArticle>(
                          dataBean = it
                      )
                   collectLiveData.value=listDataUiState
                      },
            error = {
                val listDataUiState=ListDataUiState<CollectArticle>(
                    isException =true,
                    error = it
                )
                collectLiveData.value=listDataUiState
            }
        )
    }

    fun addCollect(id: Int,position:Int){
        request(
            block = {
                    HttpRetrofit.apiService.addCollectArticle(id)
            },
            success = {
                      val listDataUiState=CollectDataUiState<Any>(
                          dataBean = it,
                          position = position
                      )
                addCollectLiveData.value=listDataUiState

            },
            error = {
                    val listDataUiState=CollectDataUiState<Any>(
                        isException = true,
                        error = it
                    )
                addCollectLiveData.value=listDataUiState
            },
            isShowDialog = true,
            loadingMessage = "收藏中..."
        )

    }

    fun cancelCollect(id: Int,position:Int){
        request(
            block = {
                HttpRetrofit.apiService.cancelCollectArticle(id)
            },
            success = {
                val listDataUiState=CollectDataUiState<Any>(
                    dataBean = it,
                    position = position
                )
                cancelCollectLiveData.value=listDataUiState

            },
            error = {
                val listDataUiState=CollectDataUiState<Any>(
                    isException = true,
                    error = it
                )
                cancelCollectLiveData.value=listDataUiState
            },
            isShowDialog = true,
            loadingMessage = "取消收藏中..."
        )

    }


}
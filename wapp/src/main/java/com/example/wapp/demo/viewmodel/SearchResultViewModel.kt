package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.AriticleResponse
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.http.apiService

/**
 * Created by jsxiaoshui on 2021-10-25
 */
class SearchResultViewModel : BaseViewModel() {
    private var pageNo=0

    val searchResultLiveData=MutableLiveData<ListDataUiState<AriticleResponse>>()

    fun getSearchDataByKey(searchKey:String ,isRefresh:Boolean) {
        if(isRefresh){
            pageNo=0
        }
        request(
            block = {
                 apiService.getSearchDataByKey(pageNo,searchKey)
             },
            success = {
               pageNo++
                val listDataUiState=ListDataUiState<AriticleResponse>(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.getResponseData().isEmpty(),
                    hasMore = it.getResponseData().hasMore(),
                    isFirstEmpty = isRefresh&&it.getResponseData().isEmpty(),
                    listData = it.getResponseData().datas
                )
                searchResultLiveData.value=listDataUiState

            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg= it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
               searchResultLiveData.value=listDataUiState
            })
    }


}
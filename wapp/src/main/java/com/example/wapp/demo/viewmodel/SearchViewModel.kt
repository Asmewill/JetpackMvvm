package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.bean.SearchResponse
import com.example.wapp.demo.http.NetworkApi
import com.example.wapp.demo.http.apiService

/**
 * Created by jsxiaoshui on 2021/8/23
 */
class SearchViewModel:BaseViewModel() {
    val searchDataState=MutableLiveData<ListDataUiState<SearchResponse>>()

    fun getHotData(){
        request(
            block = {
                apiService.getSearchData()
            },
            success = {
                      val  listDataUiState=ListDataUiState(
                          isSuccess = true,
                          listData=it.getResponseData()
                      )
                searchDataState.value=listDataUiState
            },
            error = {
                val  listDataUiState=ListDataUiState(
                    isSuccess = false,
                    listData = arrayListOf<SearchResponse>(),
                    errorMsg = it.errorMsg
                )
                searchDataState.value=listDataUiState
            }
        )
    }
}
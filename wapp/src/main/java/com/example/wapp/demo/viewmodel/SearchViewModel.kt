package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.bean.SearchResponse
import com.example.wapp.demo.http.NetworkApi
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.utils.CacheUtil
import okhttp3.Cache

/**
 * Created by jsxiaoshui on 2021/8/23
 */
class SearchViewModel:BaseViewModel() {
    val searchDataState=MutableLiveData<ListDataUiState<SearchResponse>>()
    //val historyData=MutableLiveData<ArrayList<String>>()
    var historyData:MutableLiveData<ArrayList<String>> = MutableLiveData()// = 之间必须有空格才行，否则报错

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

    fun getHistoryData(){
        launch(
            block = {
               CacheUtil.getSearchHistoryData()
            },
            success = {
                      historyData.value=it
            },
            error = {
                //获取本地数据异常了
                print(it.message)
            }
        )

    }
}
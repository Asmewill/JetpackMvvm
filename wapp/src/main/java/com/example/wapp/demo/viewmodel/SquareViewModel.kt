package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.AriticleResponse
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.http.apiService

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class SquareViewModel:BaseViewModel() {
    var pageNo=0
    var squareLiveData: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
    var askLiveData: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()


    fun getSquareData(isRefresh:Boolean){
        if(isRefresh){
            pageNo=0
        }
        request(
            block = {
                apiService.getSquareData(pageNo)
            },
            success = {
                pageNo++
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.getResponseData().datas.isEmpty(),
                    hasMore = it.getResponseData().hasMore(),
                    isFirstEmpty = isRefresh&& it.getResponseData().datas.isEmpty(),
                    listData = it.getResponseData().datas
                )
                squareLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg= it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
                squareLiveData.value=listDataUiState

            }
        )


    }

    fun  getAskData(isRefresh:Boolean){
        if(isRefresh){
            pageNo=0
        }
        request(
            block = {
                apiService.getAskData(pageNo)
            },
            success = {
                pageNo++
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.getResponseData().datas.isEmpty(),
                    hasMore = it.getResponseData().hasMore(),
                    isFirstEmpty = isRefresh&& it.getResponseData().datas.isEmpty(),
                    listData = it.getResponseData().datas
                )
                askLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg= it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
                askLiveData.value=listDataUiState
            }
        )
    }
    fun getSystemData(){
        request(
            block={
                  apiService.getSystemData()
            },
            success = {

            },
            error = {

            }
        )
    }


}
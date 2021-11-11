package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.AriticleResponse
import com.example.wapp.demo.bean.ClassifyResponse
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.http.apiService

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class WechatViewModel:BaseViewModel() {


    var publicLiveData= MutableLiveData<ListDataUiState<ClassifyResponse>>()
    var pageNo=0
    var publicDataLiveData= MutableLiveData<ListDataUiState<AriticleResponse>>()
    fun getPublicTitle(){
        request(
            block = {
                apiService.getPublicTitle()
            },
            success = {
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    listData = it.getResponseData()
                )
                publicLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg= it.errorMsg,
                    listData = arrayListOf<ClassifyResponse>()
                )
                publicLiveData.value=listDataUiState
            }
        )
    }

    fun getPublicData(isRefresh:Boolean,cid:Int){
        if(isRefresh){
            pageNo=0
        }
        request(
            block = {
                apiService.getPublicData(pageNo,cid)
            },
            success = {
                pageNo++
                 val listDataUiState=ListDataUiState<AriticleResponse>(
                     isSuccess = true,
                     isRefresh = isRefresh,
                     isEmpty = it.getResponseData().datas.isEmpty(),
                     hasMore = it.getResponseData().hasMore(),
                     isFirstEmpty = isRefresh&& it.getResponseData().datas.isEmpty(),
                     listData = it.getResponseData().datas
                 )
                publicDataLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg= it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
                publicDataLiveData.value=listDataUiState

            }
        )
    }

}
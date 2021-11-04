package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.AriticleResponse
import com.example.wapp.demo.bean.ClassifyResponse
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.http.HttpRequestManager
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.http.httpRequestManager

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class ProjectViewModel:BaseViewModel() {

     var pageNo=0
     var projectLiveData: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
     var titleLiveData: MutableLiveData<ListDataUiState<ClassifyResponse>> = MutableLiveData()
     fun getProjectTitle(){
         request(
             block = {  apiService.getProjectTitle() },
             success = {
                 val listDataUiState=ListDataUiState(
                     isSuccess = true,
                     listData = it.getResponseData()
                 )
                 titleLiveData.value=listDataUiState

             },
             error = {
                 val listDataUiState=ListDataUiState(
                     isSuccess = false,
                     errorMsg = it.errorMsg,
                     listData = arrayListOf<ClassifyResponse>()
                 )
                 titleLiveData.value=listDataUiState
             }
         )
     }

    fun getProjectData(isRefresh:Boolean,cid:Int,isNew:Boolean){
        if(isRefresh){
            pageNo = if(isNew){
                0
            }else{
                1
            }
        }
        request(
            block = {
               if(isNew){
                    apiService.getProjectNewData(pageNo)
                }else{
                    apiService.getProjectDataByType(pageNo,cid)
                }
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
                projectLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    errorMsg=it.errorMsg,
                    listData = arrayListOf<AriticleResponse>()
                )
                projectLiveData.value=listDataUiState
            }
        )
    }
}
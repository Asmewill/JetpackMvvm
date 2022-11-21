package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.ApiPagerResponse
import com.example.wapp.demo.bean.PointRecordBean
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.http.apiService

/**
 * Data :2022/5/26
 * Time:15:38
 * Author:shuij
 *
 */
class PointRecordViewModel:BaseViewModel(){

       val pointRecordLiveData=MutableLiveData<ListDataUiState<ApiPagerResponse<ArrayList<PointRecordBean>>>>();

      fun  getPointRecordList(pageNo:Int){
         request(
             block = {
                 apiService.getPointRecord(pageNo)
             },
             success = {
                       val listDataUiState=ListDataUiState<ApiPagerResponse<ArrayList<PointRecordBean>>>(
                           isSuccess =true,
                           response = it
                       )
                      pointRecordLiveData.value=listDataUiState;
             },
             error = {
                 val listDataUiState=ListDataUiState<ApiPagerResponse<ArrayList<PointRecordBean>>>(
                     isSuccess = false,
                     errorMsg = it.errorMsg
                 )
             }
         )
      }



}
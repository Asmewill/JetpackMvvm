package com.example.oapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.ScoreBean
import com.example.oapp.http.HttpRetrofit

/**
 * Created by jsxiaoshui on 2021/7/26
 */
class ScoreListViewModel: BaseViewModel() {
    val scoreListLiveData=MutableLiveData<ListDataUiState<ScoreBean>>()


    fun getScoreList(pageNo:Int){
         request(
             block = {HttpRetrofit.apiService.getScoreList(pageNo)},
             success = {
                      val  ListDataUiState=ListDataUiState<ScoreBean>(
                          dataBean =it,
                          pageNo = pageNo
                      )
                 scoreListLiveData.value=ListDataUiState
             },
             error = {
                 val  ListDataUiState=ListDataUiState<ScoreBean>(
                    isException = true,
                     error =it
                 )
                 scoreListLiveData.value=ListDataUiState
             },
             isShowDialog = false,
             loadingMessage = "loading...please wait ..."
         )



    }




}
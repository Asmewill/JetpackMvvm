package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.BannerResponse
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.bean.UserInfo
import com.example.wapp.demo.http.apiService

/**
 * Created by jsxiaoshui on 2021-11-12
 */
class LoginRegisterViewModel:BaseViewModel() {

    var loginLiveData: MutableLiveData<ListDataUiState<UserInfo>> = MutableLiveData()

    fun goLogin(userName:String,pwd:String){
         request(
             block = {apiService.login(userName,pwd)},
             success = {
                 val listDataUiState=ListDataUiState(
                     isSuccess = true,
                     listData = arrayListOf<UserInfo>(it.getResponseData())
                 )
                 loginLiveData.value=listDataUiState
             },
             error = {
                 val listDataUiState=ListDataUiState(
                     isSuccess = false,
                     errorMsg = it.errorMsg,
                     listData = arrayListOf<UserInfo>()
                 )
                 loginLiveData.value=listDataUiState
             }
         )

    }






}
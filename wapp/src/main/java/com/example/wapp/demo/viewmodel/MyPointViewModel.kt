package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.*
import com.example.wapp.demo.http.apiService

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class MyPointViewModel:BaseViewModel() {
    var pointLiveData: MutableLiveData<ListDataUiState<ApiPagerResponse<ArrayList<Point>>>> = MutableLiveData()

    fun getMyPointList(pageNo:Int){
        request(
            block = {
                 apiService.getPointList(pageNo)
            },
            success = {

                      val listDataUiState= ListDataUiState<ApiPagerResponse<ArrayList<Point>>>(
                          isSuccess = true,
                          response = it
                      )
                pointLiveData.value=listDataUiState
            },
            error = {
                    val listDataUiState=ListDataUiState<ApiPagerResponse<ArrayList<Point>>>(
                        isSuccess = false,
                        errorMsg = it.errorMsg
                    )
                pointLiveData.value=listDataUiState
            },
            isShowLoading = false,
            loadingMsg = "正在加载中...请稍后"
        )
    }
}
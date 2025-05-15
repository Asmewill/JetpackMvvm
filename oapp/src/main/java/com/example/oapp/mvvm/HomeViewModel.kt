package com.example.oapp.mvvm

import androidx.lifecycle.MutableLiveData
import com.example.oapp.bean.HomeData
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.http.HttpRetrofit

/**
 * Created by Owen on 2025/4/19
 */
class HomeViewModel : BaseViewModel() {
    val homeLiveData= MutableLiveData<ListDataUiState<ArrayList<HomeData.DatasBean>>>()
    fun getHomeList() {
        request(
            block = {  HttpRetrofit.apiService.getHomeList() },
            success = {
                val listDataUiState=ListDataUiState<ArrayList<HomeData.DatasBean>>(
                    dataBean =it
                )
                homeLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ArrayList<HomeData.DatasBean>>(
                    isException = true,
                    error = it
                )
                homeLiveData.value=listDataUiState
            },
            isShowDialog = true,
            loadingMessage = "加载中..."
        )
    }
}
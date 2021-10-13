package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.AriticleResponse
import com.example.wapp.demo.bean.BannerResponse
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.http.httpRequestManager

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class HomeViewModel:BaseViewModel() {
    var pageNo=0
    var homeDataState:MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
    var bannerDataState:MutableLiveData<ListDataUiState<BannerResponse>> = MutableLiveData()

    /**
     * 获取置顶列表和首页列表数据
     */
    fun getHomeData(isRefresh:Boolean){
        if(isRefresh){
            pageNo=0
        }
        request(
            block = { httpRequestManager.getHomeData(pageNo)},
            success = {
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.getResponseData().datas.isEmpty(),
                    hasMore = it.getResponseData().hasMore(),
                    isFirstEmpty = isRefresh&& it.getResponseData().datas.isEmpty(),
                    listData = it.getResponseData().datas
                )
                homeDataState.value=listDataUiState
            },
            error ={
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg= it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
                homeDataState.value=listDataUiState
            }
        )
    }
    /**
     * 获取banner图数据
     */
    fun getBannerData(){
        request(
            block = {
                apiService.getBanner()
                    },
            success = {
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    listData = it.getResponseData()
                )
                bannerDataState.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    listData = arrayListOf<BannerResponse>()
                )
                bannerDataState.value=listDataUiState
            }
        )
    }
}
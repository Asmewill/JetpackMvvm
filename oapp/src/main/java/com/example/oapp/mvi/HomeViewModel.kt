package com.example.oapp.mvi

import androidx.lifecycle.MutableLiveData
import com.example.oapp.bean.HomeData
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.http.HttpRetrofit
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by Owen on 2025/4/19
 */
class HomeViewModel : BaseViewModel() {
    var page=0
   // val homeLiveData= MutableLiveData<ListDataUiState<ArrayList<HomeData.DatasBean>>>()
    val homeStateFlow = MutableStateFlow<ListDataUiState<ArrayList<HomeData.DatasBean>>>(
        ListDataUiState(
            isException = false
        )
    )
    fun dispatchIntent(intent:HomeIntent){
        when(intent ){
            is HomeIntent.GetHomeList->{ //刷新
                getHomeList(page)
            }
            is HomeIntent.GetHomeListMore->{ //上拉
                page++
                getHomeList(page)
            }else->{

            }
        }
    }

    fun getHomeList(page:Int) {
        request(
            block = {  HttpRetrofit.apiService.getHomeList() },
            success = {
                val listDataUiState=ListDataUiState<ArrayList<HomeData.DatasBean>>(
                    dataBean =it
                )
                homeStateFlow.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ArrayList<HomeData.DatasBean>>(
                    isException = true,
                    error = it
                )
                homeStateFlow.value=listDataUiState
            },
            isShowDialog = true,
            loadingMessage = "加载中..."
        )
    }
}
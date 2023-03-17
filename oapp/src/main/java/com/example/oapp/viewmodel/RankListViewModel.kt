package com.example.oapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.PointBean
import com.example.oapp.http.HttpRetrofit
import kotlinx.coroutines.Job

/**
 * Unable to create call adapter for class java.lang.Object
for method ApiService.getPointList
 * Created by jsxiaoshui on 2021/7/22
 */
class RankListViewModel:BaseViewModel() {
    var  rankListLiveData = MutableLiveData<ListDataUiState<PointBean>> ()
    private var job:Job?=null
    fun getHomeRankList(pageNo:Int) {
        job= request(
            block = { HttpRetrofit.apiService.getPointList(pageNo) },
            success = {
              val listDataUiState=  ListDataUiState<PointBean>(
                  dataBean = it,
                  pageNo = pageNo
              )
              rankListLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=  ListDataUiState<PointBean>(
                    isException = true,
                    error = it,
                    pageNo = pageNo
                )
                rankListLiveData.value=listDataUiState
            }
        )
    }
    override fun onCleared() {
        super.onCleared()
        job?.let {
            it.cancel()
        }
    }
}
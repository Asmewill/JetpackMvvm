package com.example.wapp.demo.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.bean.PointBean
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.utils.ColorUtil

/**
 * Created by jsxiaoshui on 2021/8/20
 *
 */
class MineViewModel:BaseViewModel() {
    var name=ObservableField<String>("请先登录~")

    var point=ObservableField<Int>(0)

    var info=ObservableField<String>("id:-- 排名:--")

    var imageUrl=ObservableField<String>(ColorUtil.randomImage())

    var pointLiveData: MutableLiveData<ListDataUiState<PointBean>> = MutableLiveData()

    fun getPointAndRank(){
        request(
            block = { apiService.getPointAndRank()},
            success = {
                val listDataUiState=ListDataUiState(
                    isSuccess = it.getStatus(),
                    errorMsg = it.getResponseMes(),
                    listData = arrayListOf(it.getResponseData())
                )
                pointLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg = it.errorMsg,
                    listData = arrayListOf<PointBean>()
                )
                pointLiveData.value=listDataUiState
            }
        )
    }


}
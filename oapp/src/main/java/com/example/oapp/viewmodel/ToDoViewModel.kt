package com.example.oapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.ToDoBean
import com.example.oapp.http.HttpRetrofit

/**
 * Created by jsxiaoshui on 2021/7/27
 */
class ToDoViewModel:BaseViewModel() {
     val deleteLiveData=MutableLiveData<ListDataUiState<ToDoBean.DatasBean>>()
    val toToLiveData=MutableLiveData<ListDataUiState<ToDoBean>>()
    val markHaveDoneLiveData=MutableLiveData<ListDataUiState<ToDoBean.DatasBean>>()
    fun getTodoList(pageNo:Int){
        request(
           block = {HttpRetrofit.apiService.getTodoList("listnotdo",0,pageNo)},
            success ={
                     val listDataUiState=ListDataUiState<ToDoBean>(
                         pageNo = pageNo,
                         dataBean = it
                     )
                toToLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ToDoBean>(
                    isException = true,
                    error = it
                )
                toToLiveData.value=listDataUiState
            },
            isShowDialog = false,
            loadingMessage = "loading"
        )
    }

    fun markHaveDone( type:String,id:Int,status:Int){
        request(
            block = { HttpRetrofit.apiService.markOrDelete(type,id,status)},
            success = {
                      val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                          dataBean = it
                      )
                markHaveDoneLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                     isException = true,
                     error = it
                )
                markHaveDoneLiveData.value=listDataUiState
            },
            isShowDialog = true

        )
    }
    fun delete(type:String,id:Int){
        request(
            block = {HttpRetrofit.apiService.delete("delete",id)},
            success = {
                      val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                          dataBean = it
                      )
                deleteLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                     error=it,
                     isException = true
                )
                deleteLiveData.value=listDataUiState
            },
            isShowDialog = true
        )

    }

    override fun onCleared() {
        super.onCleared()
    }
}
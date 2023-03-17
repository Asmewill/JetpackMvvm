package com.example.oapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.ToDoBean
import com.example.oapp.http.HttpRetrofit

/**
 * Created by jsxiaoshui on 2021/7/27
 */
class DoneViewModel:BaseViewModel() {

     val deleteLiveData=MutableLiveData<ListDataUiState<ToDoBean.DatasBean>>()
    val resumeLiveData=MutableLiveData<ListDataUiState<ToDoBean.DatasBean>>()
    val doneListLiveData=MutableLiveData<ListDataUiState<ToDoBean>>()

    fun getDoneList(pageNo:Int){
        request(
            block = {HttpRetrofit.apiService.getTodoList("listdone",0,1)},
            success = {
                      val listDataUiState=ListDataUiState<ToDoBean>(
                          dataBean = it
                      )
                   doneListLiveData.value=listDataUiState

            },
            error = {
                val listDataUiState=ListDataUiState<ToDoBean>(
                     isException = true,
                     error = it
                )
                doneListLiveData.value=listDataUiState
            }
        )
    }


    fun resume( type:String,id:Int,status:Int){
        request(
            block = { HttpRetrofit.apiService.markOrDelete(type,id,status)},
            success = {
                val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                    dataBean = it
                )
                resumeLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                    isException = true,
                    error = it
                )
                resumeLiveData.value=listDataUiState
            }
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
            }
        )

    }
}
package com.example.oapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.ToDoBean
import com.example.oapp.http.HttpRetrofit

/**
 * Created by jsxiaoshui on 2021/7/28
 */
class AddToDoViewModel:BaseViewModel() {

    val addTodoLiveData=MutableLiveData<ListDataUiState<ToDoBean.DatasBean>>()

    val editTodoLiveData=MutableLiveData<ListDataUiState<ToDoBean.DatasBean>>()

    fun saveTodoPlane(type:Int, title:String, content:String, prority:String, date:String){
        request(
            block = {HttpRetrofit.apiService.addToDo(type,title,content,date,prority)},
            success = {
                      val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                          dataBean =it
                      )
                  addTodoLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                    isException = true,
                    error = it
                )
                addTodoLiveData.value=listDataUiState
            },
            isShowDialog = false,
            loadingMessage = "loading"
        )
    }

    fun editTodoPlane(id:Int,type:Int, title:String, content:String, prority:String, date:String){
        request(
            block = {HttpRetrofit.apiService.editToDo(id,type,title,content,date,prority)},
            success = {
                val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                    dataBean =it
                )
                editTodoLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState<ToDoBean.DatasBean>(
                    isException = true,
                    error = it
                )
                editTodoLiveData.value=listDataUiState
            },
            isShowDialog = true,
            loadingMessage = "loading"
        )
    }
}
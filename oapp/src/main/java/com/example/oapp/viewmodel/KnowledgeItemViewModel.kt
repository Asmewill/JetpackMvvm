package com.example.oapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.oapp.base.BaseViewModel
import com.example.oapp.bean.KnowItemListBean
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.ToDoBean
import com.example.oapp.http.HttpRetrofit
import org.jetbrains.anko.db.TEXT

/**
 * Created by jsxiaoshui on 2021/8/2
 */
class KnowledgeItemViewModel:BaseViewModel() {


     val knowItemLiveData=MutableLiveData<ListDataUiState<KnowItemListBean>>()

    fun  getKnowledgeItemList(pageNo:Int, cid:Int){
        request(
            block = {HttpRetrofit.apiService.getKnowledgeList(pageNo,cid)},
            success = {
                val listDataUiState= ListDataUiState<KnowItemListBean>(
                    dataBean = it
                )
                knowItemLiveData.value=listDataUiState

            },
            error = {
                val listDataUiState= ListDataUiState<KnowItemListBean>(
                     isException = true,
                     error = it
                )
                knowItemLiveData.value=listDataUiState
            },
            isShowDialog = false,
            loadingMessage = "loading..."
        )

    }
}
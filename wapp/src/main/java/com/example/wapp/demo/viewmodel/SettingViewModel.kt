package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.Utils
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.ListDataUiState
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class SettingViewModel:BaseViewModel(){
    var loginOutHxLiveData: MutableLiveData<ListDataUiState<Any>> = MutableLiveData()
    fun loginOutHx(unbindDeviceToken:Boolean){
        requestHX(
            block = {
                EMClient.getInstance().logout(unbindDeviceToken, object : EMCallBack {
                    override fun onSuccess() {
                        val listDataUiState=ListDataUiState(
                            isSuccess = true,
                            listData = arrayListOf<Any>()
                        )
                        Utils.runOnUiThread{
                            loginOutHxLiveData.value=listDataUiState
                        }
                    }

                    override fun onProgress(progress: Int, status: String) {

                    }

                    override fun onError(code: Int, error: String) {
                        val listDataUiState=ListDataUiState(
                            isSuccess = false,
                            errorMsg = error,
                            listData = arrayListOf<Any>()
                        )
                        Utils.runOnUiThread{
                            loginOutHxLiveData.value=listDataUiState
                        }
                    }
                })

            }
        )
    }
}
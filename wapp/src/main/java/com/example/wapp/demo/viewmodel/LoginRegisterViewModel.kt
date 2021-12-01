package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.bean.UserInfo
import com.example.wapp.demo.http.apiService
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.easeui.domain.EaseUser

/**
 * Created by jsxiaoshui on 2021-11-12
 */
class LoginRegisterViewModel:BaseViewModel() {

    var loginLiveData: MutableLiveData<ListDataUiState<UserInfo>> = MutableLiveData()
    var registerLiveData: MutableLiveData<ListDataUiState<Any>> = MutableLiveData()
    var registerHxLiveData: MutableLiveData<ListDataUiState<String>> = MutableLiveData()
    var loginHxLiveData: MutableLiveData<ListDataUiState<EaseUser>> = MutableLiveData()
    fun goLogin(userName:String,pwd:String){
         request(
             block = {apiService.login(userName,pwd)},
             success = {
                 val listDataUiState=ListDataUiState(
                     isSuccess = it.getStatus(),
                     errorMsg = it.getResponseMes(),
                     listData = arrayListOf<UserInfo>(it.getResponseData())
                 )
                 loginLiveData.value=listDataUiState
             },
             error = {
                 val listDataUiState=ListDataUiState(
                     isSuccess = false,
                     errorMsg = it.errorMsg,
                     listData = arrayListOf<UserInfo>()
                 )
                 loginLiveData.value=listDataUiState
             },
             isShowLoading = true,
             loadingMsg = "登录中..."
         )

    }

    fun register(userName:String,pwd:String){
        request(
            block={
                apiService.register(userName,pwd,pwd)
            },
            success = {
                val listDataUiState=ListDataUiState(
                    isSuccess = it.getStatus(),
                    errorMsg = it.getResponseMes(),
                    listData = arrayListOf<Any>(it.getResponseData())
                )
                registerLiveData.value=listDataUiState

            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg = it.errorMsg,
                    listData = arrayListOf<Any>()
                )
                registerLiveData.value=listDataUiState

            },
            isShowLoading = true,
            loadingMsg = "注册中..."
        )
    }

    fun registerHx(userName: String,pwd: String){
        requestHX(
            block = {
                EMClient.getInstance().createAccount(userName, pwd)
            },
            success = {
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    listData = arrayListOf<String>(userName)
                )
                registerHxLiveData.value=listDataUiState
            },
            error = {
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg=it.message.toString(),
                    listData = arrayListOf<String>()
                )
                registerHxLiveData.value=listDataUiState
            }
        )
    }


    fun loginHx(userName: String,pwd: String){
        requestHX(
            block = {
                EMClient.getInstance().login(userName, pwd, object : EMCallBack {
                    override fun onSuccess() {
                        val currentUser = EMClient.getInstance().currentUser
                        val user = EaseUser(currentUser)
                        val listDataUiState=ListDataUiState(
                            isSuccess = true,
                            listData = arrayListOf<EaseUser>(user)
                        )
                        Utils.runOnUiThread{
                            loginHxLiveData.value=listDataUiState
                        }

                    }
                    override fun onError(code: Int, error: String) {
                        val listDataUiState=ListDataUiState(
                            isSuccess = false,
                            errorMsg=error,
                            listData = arrayListOf<EaseUser>()
                        )
                        Utils.runOnUiThread{
                            loginHxLiveData.value=listDataUiState
                        }
                    }
                    override fun onProgress(progress: Int, status: String?) {

                    }
                })
            }
        )
    }
}
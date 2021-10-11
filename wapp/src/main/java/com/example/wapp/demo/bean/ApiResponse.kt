package com.example.wapp.demo.bean

/**
 * Created by jsxiaoshui on 2021-10-11
 */
data class ApiResponse<T>(val errorCode:Int,val errorMsg:String,val data:T) :BaseResponse<T>(){
    override fun getResponseData(): T {
        return  data
    }

    override fun getStatus(): Boolean {
        return errorCode==0
    }

    override fun getrResponseCode(): Int {
        return  errorCode
    }

    override fun getResponseMes(): String {
        return  errorMsg;
    }
}
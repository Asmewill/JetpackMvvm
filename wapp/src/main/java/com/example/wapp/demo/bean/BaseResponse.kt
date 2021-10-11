package com.example.wapp.demo.bean

/**
 * Created by jsxiaoshui on 2021-10-11
 */
abstract  class BaseResponse<T> {
   abstract fun getResponseData():T
   abstract fun getStatus():Boolean
   abstract fun getrResponseCode():Int
   abstract fun getResponseMes():String
}
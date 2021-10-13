package com.example.wapp.demo.bean.exception

/**
 * Created by jsxiaoshui on 2021-10-13
 */
class AppException:Exception {
    var errorCode:Int=0
    var errorMsg:String
    var errorLog:String?

    constructor(errorCode: Int, errorMsg: String, errorLog: String) : super(errorMsg) {
        this.errorCode = errorCode
        this.errorMsg = errorMsg
        this.errorLog = errorLog
    }
    constructor(error: Error,e: Throwable?) {
        errorCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
    }
}
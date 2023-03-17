package com.example.oapp.http

/**
 * Created by jsxiaoshui on 2021/6/28
 */
interface ApiCallback<T> {
    fun onSuccess(t:T)
    fun onFailture(t:Throwable)
}
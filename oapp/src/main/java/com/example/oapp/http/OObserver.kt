package com.example.oapp.http

import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.LoginBean
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by jsxiaoshui on 2021/6/28
 */
 open class OObserver<T>(private var apiCallBack:ApiCallback<T>):Observer<T> {
    override fun onSubscribe(p0: Disposable) {

    }
    override fun onNext(p0: T) {
        apiCallBack.onSuccess(p0);
    }

    override fun onError(p0: Throwable) {
        apiCallBack.onFailture(p0)
    }

    override fun onComplete() {

    }
}
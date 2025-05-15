package com.example.oapp.mvp;


import io.reactivex.functions.Consumer;

/**
 * Created by Owen on 2025/4/18
 */
class OConsumer<T> implements Consumer<T> {
    SuccessCallBack<T> successCallBack;
    public OConsumer(SuccessCallBack<T> successCallBack) {
        this.successCallBack=successCallBack;
    }

    @Override
    public void accept(T t) throws Exception {
        //可以在这里根据code码，做统一处理
        successCallBack.onSuccess(t);
    }
}

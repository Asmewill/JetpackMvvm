package com.example.oapp.mvp;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Owen on 2025/4/18
 */
public  class BasePrenster<T extends BaseView> {
    T mView;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
   public void detachView(){
       compositeDisposable.dispose();  //compositeDisposable.clear();
       mView=null;
   }

}

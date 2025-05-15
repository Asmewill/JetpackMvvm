package com.example.oapp.mvp;

import com.example.oapp.bean.HomeData;
import com.example.oapp.bean.HttpResult;
import com.example.oapp.http.HttpRetrofit;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Owen on 2025/4/18
 */
class HomePresenter extends BasePrenster<HomeView> {

   public HomePresenter(HomeView homeView){
       this.mView=homeView;
   }
    public void getHomeData() {
        mView.showLoading();
        compositeDisposable.add(HttpRetrofit.INSTANCE.getApiService().getTopArticles()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OConsumer<HttpResult<ArrayList<HomeData.DatasBean>>>(new SuccessCallBack<HttpResult<ArrayList<HomeData.DatasBean>>>() {
                    @Override
                    public void onSuccess(HttpResult<ArrayList<HomeData.DatasBean>> httpResult) {
                        mView.onHomeDataSuccess(httpResult);
                        mView.hideLoading();
                    }
                }), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.hideLoading();
                        mView.onHomeDataFailed(throwable.getMessage());
                    }
                }));
    }

}

package com.example.oapp.mvp;

import com.example.oapp.bean.HomeData;
import com.example.oapp.bean.HttpResult;

import java.util.ArrayList;

/**
 * Created by Owen on 2025/4/18
 */
class HomeActivity extends BaseMvpActivity<HomePresenter> implements HomeView{
    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected void initData() {
        mPresenter.getHomeData();

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onHomeDataSuccess(HttpResult<ArrayList<HomeData.DatasBean>> httpResult) {


    }

    @Override
    public void onHomeDataFailed(String errorMsg) {

    }
}

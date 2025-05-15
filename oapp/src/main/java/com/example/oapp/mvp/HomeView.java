package com.example.oapp.mvp;

import com.example.oapp.bean.HomeData;
import com.example.oapp.bean.HttpResult;

import java.util.ArrayList;

/**
 * Created by Owen on 2025/4/18
 */
interface  HomeView extends BaseView {

    void onHomeDataSuccess(HttpResult<ArrayList<HomeData.DatasBean>> httpResult);
    void onHomeDataFailed(String errorMsg);
}

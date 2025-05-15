package com.example.oapp.mvvm

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.oapp.R
import com.example.oapp.databinding.ActivityHomeBinding

/**
 * Created by Owen on 2025/4/19
 */
class HomeActivity:BaseVMActivity<HomeViewModel>() {
    lateinit var mDataBind:ActivityHomeBinding

    override fun createViewModel(): HomeViewModel {
         return ViewModelProvider(this).get(HomeViewModel::class.java);
    }

    override fun initData() {
        mDataBind = DataBindingUtil.setContentView(this, R.layout.activity_home) //实例化mDataBind
        mViewModel.getHomeList() //请求数据
        //添加观察者,处理数据
        mViewModel.homeLiveData.observe(this) { //处理数据
            mDataBind.homeData=it;//数据双向绑定
       }
    }
}
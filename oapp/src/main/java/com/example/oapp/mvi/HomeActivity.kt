package com.example.oapp.mvi

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.oapp.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Owen on 2025/4/19
 */
class HomeActivity:BaseVMActivity<HomeViewModel>() {
   // lateinit var mDataBind:ActivityHomeBinding

    override fun createViewModel(): HomeViewModel {
         return ViewModelProvider(this).get(HomeViewModel::class.java);
    }

    override fun initData() {
       // mDataBind = DataBindingUtil.setContentView(this, R.layout.activity_home) //实例化mDataBind
        mViewModel.dispatchIntent(HomeIntent.GetHomeList) //请求第一页数据
       // mViewModel.dispatchIntent(HomeIntent.GetHomeListMore) //上拉加载下一页数据
        //添加观察者,处理数据
        lifecycleScope.launch {
            mViewModel.homeStateFlow.collect {


            }
        }
    }
}
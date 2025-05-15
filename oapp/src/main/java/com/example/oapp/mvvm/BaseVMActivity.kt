package com.example.oapp.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Owen on 2025/4/19
 */
abstract class BaseVMActivity<VM : BaseViewModel>:AppCompatActivity() {
    lateinit var mViewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel=createViewModel()
        initData();
    }

    abstract fun createViewModel(): VM
    abstract fun initData()
}
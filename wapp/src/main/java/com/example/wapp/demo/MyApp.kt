package com.example.wapp.demo

import android.app.Application
import com.kingja.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.*

/**
 * Created by jsxiaoshui on 2021/8/17
 */
class MyApp : Application() {

    companion object{
        lateinit var instance :MyApp
    }
    override fun onCreate() {
        super.onCreate()
        instance=this
        initLoadSir()

    }

    private fun initLoadSir() {
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(LoadingCallback2())
            .addCallback(ErrorCallback())//错误
            .addCallback(ErrorCallback2())//错误
            .addCallback(EmptyCallback())//空
            .addCallback(EmptyCallback2())
            //.setDefaultCallback(LoadingCallback::class.java)//设置默认加载状态页
            .commit()

    }

}

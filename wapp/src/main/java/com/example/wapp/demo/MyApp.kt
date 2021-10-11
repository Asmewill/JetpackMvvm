package com.example.wapp.demo

import android.app.Application

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
    }

}

package com.example.wapp.demo.utils

import android.text.TextUtils
import com.example.wapp.demo.bean.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

/**
 * Created by jsxiaoshui on 2021-10-25
 */
object CacheUtil {

    //获取历史记录列表
   fun getSearchHistoryData():ArrayList<String> {
       val kv=MMKV.mmkvWithID("cache")
       val searchCacheStr=kv.decodeString("history")
       if(!TextUtils.isEmpty(searchCacheStr)){
          return Gson().fromJson(searchCacheStr,object :TypeToken<ArrayList<String>>(){}.type)
       }
       return  arrayListOf()
   }
    fun setSearchHistoryData(searchResponseStr:String){
        val kv=MMKV.mmkvWithID("cache")
        kv.encode("history",searchResponseStr)
    }

    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
    }


    /**
     * 获取保存的账户信息
     */
    fun getUser(): UserInfo? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, UserInfo::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: UserInfo?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
            setIsLogin(false)
        } else {
            kv.encode("user", Gson().toJson(userResponse))
            setIsLogin(true)
        }
    }

}
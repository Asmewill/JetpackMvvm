package com.example.wapp.demo.utils

import android.text.TextUtils
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
}
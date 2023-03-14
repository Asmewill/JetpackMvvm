package com.example.wapp.demo.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.preference.PreferenceManager
import com.example.wapp.R
import com.tencent.mmkv.MMKV

/**
 * Data :2023/2/13
 * Time:17:36
 * Author:shuij
 *
 */
object SettingUtil {
    /**
     * 获取当前主题颜色
     */
    fun getColor(context:Context):Int{
        val setting=PreferenceManager.getDefaultSharedPreferences(context)
        val defaultColor=ContextCompat.getColor(context, R.color.colorPrimary)
        val color=setting.getInt("color",defaultColor)
        if(color!=0&&Color.alpha(color)!=255){
            return defaultColor
        }else{
           return color
        }
    }
    /**
     * 设置主题颜色
     */
    fun getListMode():Int{
        val kv= MMKV.mmkvWithID("app")
        return kv.decodeInt("mode",2)
    }
}
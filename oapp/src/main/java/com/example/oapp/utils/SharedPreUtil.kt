package com.example.oapp.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by LiuJie on 2015/12/21.
 */
object SharedPreUtil {
    const val SHAREDPREF_URI = "share_ip_port"
    private var sharedPreUtil: SharedPreferences? = null

    //缓存key
    const val KEY_URI = "key_ip_port"
    const val KEY_INFO = "key_info"

    //储位缓存
    const val KEY_USELOCATION = "key_uselocation"

    //入库单缓存
    const val KEY_INMAKE_LOCALPROD = "key_inmake_orders"

    //工单备料，备料单缓存
    const val KEY_SCMAKE_PREPARE = "key_scmake_prepare"

    //飞达上料，备料单缓存
    const val KEY_SCMAKE_FEEDER = "key_scmake_feeder"

    //清除IP和PORT的所有缓存
    fun removeAll(pContext: Context) {
        if (sharedPreUtil == null) {
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0)
        }
        //清除储位缓存
        sharedPreUtil!!.edit().remove(KEY_USELOCATION).commit()
        //清除入库单缓存
        sharedPreUtil!!.edit().remove(KEY_INMAKE_LOCALPROD).commit()
        //清除备料单缓存
        sharedPreUtil!!.edit().remove(KEY_SCMAKE_PREPARE).commit()
        //清除飞达上料缓存
        sharedPreUtil!!.edit().remove(KEY_SCMAKE_FEEDER).commit()
    }

    fun saveString(pContext: Context, key: String?, value: String?) {
        if (sharedPreUtil == null) {
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0)
        }
        val `is` = sharedPreUtil!!.edit().putString(key, value).commit()
    }

    fun removeString(pContext: Context, key: String?) {
        if (sharedPreUtil == null) {
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0)
        }
        sharedPreUtil!!.edit().remove(key).commit()
    }

    fun getString(pContext: Context, key: String, defaultStr: String?): String? {
        if (sharedPreUtil == null) {
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0)
        }
        return sharedPreUtil!!.getString(key, defaultStr)
    }

    fun saveInt(context: Context, key: String?, value: Int) {
        if (sharedPreUtil == null) {
            sharedPreUtil = context.getSharedPreferences(SHAREDPREF_URI, 0)
        }
        sharedPreUtil!!.edit().putInt(key, value).commit()
    }

    fun getInt(context: Context, key: String?, defaultInt: Int): Int {
        if (sharedPreUtil == null) {
            sharedPreUtil = context.getSharedPreferences(SHAREDPREF_URI, 0)
        }
        return sharedPreUtil!!.getInt(key, defaultInt)
    }

    fun removeInt(context: Context, key: String?) {
        if (sharedPreUtil == null) {
            sharedPreUtil = context.getSharedPreferences(SHAREDPREF_URI, 0)
        }
        sharedPreUtil!!.edit().remove(key).commit()
    }
}
package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * 积分
 */

data class IntegralResponse(
        var coinCount: Int,//当前积分
        var rank: Int,
        var userId: Int,
        var username: String) : Serializable



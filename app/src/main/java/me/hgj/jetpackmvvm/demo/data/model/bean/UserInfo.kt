package me.hgj.jetpackmvvm.demo.data.model.bean
import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 账户信息
 */

data class UserInfo(var admin: Boolean = false,
                    var chapterTops: List<String> = listOf(),
                    var collectIds: MutableList<String> = mutableListOf(),
                    var email: String="",
                    var icon: String="",
                    var id: String="",
                    var nickname: String="",
                    var password: String="",
                    var token: String="",
                    var type: Int =0,
                    var username: String="") : Serializable

package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * 导航数据
  * @Author:         hegaojian
  * @CreateDate:     2019/8/26 17:40
 */

data class NavigationResponse(var articles: ArrayList<AriticleResponse>,
                              var cid: Int,
                              var name: String) : Serializable

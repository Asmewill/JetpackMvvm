package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * 文章的标签
 */

data class TagsResponse(var name:String, var url:String): Serializable

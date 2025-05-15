package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *  体系数据
  * @Author:         hegaojian
  * @CreateDate:     2019/8/21 15:46
 */

data class SystemResponse(var children: ArrayList<ClassifyResponse>,
                          var courseId: Int,
                          var id: Int,
                          var name: String,
                          var order: Int,
                          var parentChapterId: Int,
                          var userControlSetTop: Boolean,
                          var visible: Int) : Serializable

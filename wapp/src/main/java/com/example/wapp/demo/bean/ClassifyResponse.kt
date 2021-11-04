package com.example.wapp.demo.bean

import java.io.Serializable

data class ClassifyResponse(
    var children: List<Any>,
    var courseId: Int, // 13
    var id: Int, // 294
    var name: String, // 完整项目
    var order: Int, // 145000
    var parentChapterId: Int, // 293
    var userControlSetTop: Boolean, // false
    var visible: Int // 0
):Serializable
package com.example.wapp.demo.bean

data class SystemResponse(
    var children: List<Children>,
    var courseId: Int, // 13
    var id: Int, // 150
    var name: String, // 开发环境
    var order: Int, // 1
    var parentChapterId: Int, // 0
    var userControlSetTop: Boolean, // false
    var visible: Int // 1
) {
    data class Children(
        var children: List<Any>,
        var courseId: Int, // 13
        var id: Int, // 60
        var name: String, // Android Studio相关
        var order: Int, // 1000
        var parentChapterId: Int, // 150
        var userControlSetTop: Boolean, // false
        var visible: Int // 1
    )
}
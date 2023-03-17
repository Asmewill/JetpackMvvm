package com.example.oapp.bean

import java.io.Serializable

/**
 * Created by jsxiaoshui on 2021/6/28
 */
class Banner : Serializable {
    /**
     * desc : 扔物线
     * id : 29
     * imagePath : https://wanandroid.com/blogimgs/31c2394c-b07c-4699-afd1-95aa7a3bebc6.png
     * isVisible : 1
     * order : 0
     * title : View 嵌套太深会卡？来用 Jetpack Compose，随便套Compose 的 Intrinsic Measurement
     * type : 0
     * url : https://www.bilibili.com/video/BV1ZA41137gr
     */
    var desc: String? = null
    var id = 0
    var imagePath: String? = null
    var isVisible = 0
    var order = 0
    var title: String? = null
    var type = 0
    var url: String? = null
}
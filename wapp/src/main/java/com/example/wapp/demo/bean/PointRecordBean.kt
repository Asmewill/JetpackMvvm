package com.example.wapp.demo.bean

import java.io.Serializable

/**
 * Data :2022/5/26
 * Time:15:28
 * Author:shuij
 */
class PointRecordBean : Serializable {
    var coinCount = 0
    var date: Long = 0
    var desc: String? = null
    var id = 0
    var reason: String? = null
    var type = 0
    var userId = 0
    var userName: String? = null
}
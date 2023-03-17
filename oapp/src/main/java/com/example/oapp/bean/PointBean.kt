package com.example.oapp.bean

import java.io.Serializable

data class PointBean(
    val curPage: Int, // 1
    val datas: List<Data>,
    val offset: Int, // 0
    val over: Boolean, // false
    val pageCount: Int, // 2635
    val size: Int, // 30
    val total: Int // 79049
):Serializable {
    data class Data(
        val coinCount: Int, // 47007
        val level: Int, // 471
        val nickname: String,
        val rank: String, // 1
        val userId: Int, // 20382
        val username: String // g**eii
    ):Serializable
}
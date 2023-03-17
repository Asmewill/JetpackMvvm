package com.example.oapp.bean

data class ScoreBean(
    val curPage: Int, // 1
    val datas: List<Data>,
    val offset: Int, // 0
    val over: Boolean, // true
    val pageCount: Int, // 1
    val size: Int, // 20
    val total: Int // 5
) {
    data class Data(
        val coinCount: Int, // 14
        val date: Long, // 1627282346000
        val desc: String, // 2021-07-26 14:52:26 签到 , 积分：10 + 4
        val id: Int, // 471373
        val reason: String, // 签到
        val type: Int, // 1
        val userId: Int, // 104421
        val userName: String // Owen888
    )
}
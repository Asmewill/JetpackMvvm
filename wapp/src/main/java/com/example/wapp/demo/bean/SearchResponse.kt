package com.example.wapp.demo.bean

import java.io.Serializable

data class SearchResponse(
    val id: Int, // 6
    val link: String,
    val name: String, // 面试
    val order: Int, // 1
    val visible: Int // 1
):Serializable
package com.example.oapp.bean

import java.io.Serializable

data class TestData(
    val `data`: List<Data>,
    val errorCode: Int, // 0
    val errorMsg: String
):Serializable {
    data class Data(
        val apkLink: String,
        val audit: Int, // 1
        val author: String, // 扔物线
        val canEdit: Boolean, // false
        val chapterId: Int, // 249
        val chapterName: String, // 干货资源
        val collect: Boolean, // false
        val courseId: Int, // 13
        val desc: String,
        val descMd: String,
        val envelopePic: String,
        val fresh: Boolean, // false
        val host: String,
        val id: Int, // 12554
        val link: String, // https://www.bilibili.com/video/BV1CA41177Se
        val niceDate: String, // 2021-07-18 00:00
        val niceShareDate: String, // 2020-03-23 16:36
        val origin: String,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long, // 1626537600000
        val realSuperChapterId: Int, // 248
        val selfVisible: Int, // 0
        val shareDate: Long, // 1584952597000
        val shareUser: String,
        val superChapterId: Int, // 249
        val superChapterName: String, // 干货资源
        val tags: List<Tag>,
        val title: String, // Android 面试黑洞&mdash;&mdash;当我按下 Home 键再切回来，会发生什么？
        val type: Int, // 1
        val userId: Int, // -1
        val visible: Int, // 1
        val zan: Int // 0
    ):Serializable {
        data class Tag(
            val name: String, // 本站发布
            val url: String // /article/list/0?cid=440
        ):Serializable
    }
}
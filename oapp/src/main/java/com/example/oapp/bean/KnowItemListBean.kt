package com.example.oapp.bean

data class KnowItemListBean(
    val curPage: Int, // 1
    val datas: List<Data>,
    val offset: Int, // 0
    val over: Boolean, // false
    val pageCount: Int, // 2
    val size: Int, // 20
    val total: Int // 22
) {
    data class Data(
        val apkLink: String,
        val audit: Int, // 1
        val author: String,
        val canEdit: Boolean, // false
        val chapterId: Int, // 77
        val chapterName: String, // 响应式编程
        var collect: Boolean, // false
        val courseId: Int, // 13
        val desc: String,
        val descMd: String,
        val envelopePic: String,
        val fresh: Boolean, // false
        val host: String,
        val id: Int, // 12468
        val link: String, // https://blog.csdn.net/tellh/article/details/71534704
        val niceDate: String, // 2020-03-19 00:39
        val niceShareDate: String, // 2020-03-18 13:24
        val origin: String,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long, // 1584549549000
        val realSuperChapterId: Int, // 53
        val selfVisible: Int, // 0
        val shareDate: Long, // 1584509092000
        val shareUser: String, // wangzhengyi
        val superChapterId: Int, // 76
        val superChapterName: String, // 热门专题
        val tags: List<Any>,
        val title: String, // 一起来造一个RxJava，揭秘RxJava的实现原理
        val type: Int, // 0
        val userId: Int, // 38889
        val visible: Int, // 1
        val zan: Int // 0
    )
}
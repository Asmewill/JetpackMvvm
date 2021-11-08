package com.example.wapp.demo.bean

data class NavigationResponse(
    var articles: List<Article>,
    var cid: Int, // 287
    var name: String // 直播SDK
) {
    data class Article(
        var alreadyInHomePage: Boolean, // false
        var apkLink: String,
        var audit: Int, // 1
        var author: String, // 小编
        var canEdit: Boolean, // false
        var chapterId: Int, // 287
        var chapterName: String, // 直播SDK
        var collect: Boolean, // false
        var courseId: Int, // 13
        var desc: String,
        var descMd: String,
        var envelopePic: String,
        var fresh: Boolean, // false
        var host: String,
        var id: Int, // 3011
        var link: String, // https://www.upyun.com/products/live
        var niceDate: String, // 2018-01-01 14:23
        var niceShareDate: String, // 未知时间
        var origin: String,
        var prefix: String,
        var projectLink: String,
        var publishTime: Long, // 1514787828000
        var realSuperChapterId: Int, // 0
        var selfVisible: Int, // 0
        var shareUser: String,
        var superChapterId: Int, // 0
        var superChapterName: String,
        var tags: List<Any>,
        var title: String, // 又拍云 UPLive
        var type: Int, // 0
        var userId: Int, // -1
        var visible: Int, // 0
        var zan: Int // 0
    )
}
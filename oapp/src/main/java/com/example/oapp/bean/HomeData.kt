package com.example.oapp.bean

import java.io.Serializable

/**
 * Created by jsxiaoshui on 2021/6/28
 */
class HomeData : Serializable {
    /**
     * curPage : 1
     * datas : [{"apkLink":"","audit":1,"author":"郭霖","canEdit":false,"chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"host":"","id":18735,"link":"https://mp.weixin.qq.com/s/Q5efKqeaylAqOfASSo2aPQ","niceDate":"2021-06-24 00:00","niceShareDate":"2021-06-24 23:24","origin":"","prefix":"","projectLink":"","publishTime":1624464000000,"realSuperChapterId":407,"selfVisible":0,"shareDate":1624548244000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"深入浅出协程、线程和并发问题","type":0,"userId":-1,"visible":1,"zan":0}]
     * offset : 0
     * over : false
     * pageCount : 535
     * size : 20
     * total : 10697
     */
    var curPage = 0
    var offset = 0
    var over = false
    var pageCount = 0
    var size = 0
    var total = 0
    var datas: List<DatasBean>? = null

    class DatasBean : Serializable {
        /**
         * apkLink :
         * audit : 1
         * author : 郭霖
         * canEdit : false
         * chapterId : 409
         * chapterName : 郭霖
         * collect : false
         * courseId : 13
         * desc :
         * descMd :
         * envelopePic :
         * fresh : false
         * host :
         * id : 18735
         * link : https://mp.weixin.qq.com/s/Q5efKqeaylAqOfASSo2aPQ
         * niceDate : 2021-06-24 00:00
         * niceShareDate : 2021-06-24 23:24
         * origin :
         * prefix :
         * projectLink :
         * publishTime : 1624464000000
         * realSuperChapterId : 407
         * selfVisible : 0
         * shareDate : 1624548244000
         * shareUser :
         * superChapterId : 408
         * superChapterName : 公众号
         * tags : [{"name":"公众号","url":"/wxarticle/list/409/1"}]
         * title : 深入浅出协程、线程和并发问题
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */
        var apkLink: String? = null
        var audit = 0
        var author: String? = null
        var canEdit = false
        var chapterId = 0
        var chapterName: String? = null
        var collect = false
        var courseId = 0
        var desc: String? = null
        var descMd: String? = null
        var envelopePic: String? = null
        var fresh = false
        var host: String? = null
        var id = 0
        var link: String? = null
        var niceDate: String? = null
        var niceShareDate: String? = null
        var origin: String? = null
        var prefix: String? = null
        var projectLink: String? = null
        var publishTime: Long = 0
        var realSuperChapterId = 0
        var selfVisible = 0
        var shareDate: Long = 0
        var shareUser: String? = null
        var superChapterId = 0
        var superChapterName: String? = null
        var title: String? = null
        var type = 0
        var userId = 0
        var visible = 0
        var zan = 0
        var tags: List<TagsBean>? = null
        var isTop=false

        class TagsBean : Serializable {
            /**
             * name : 公众号
             * url : /wxarticle/list/409/1
             */
            var name: String? = null
            var url: String? = null
        }
    }
}
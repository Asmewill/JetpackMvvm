package com.example.oapp.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class CollectArticle(
    val curPage: Int, // 1
    val datas: List<Data>,
    val offset: Int, // 0
    val over: Boolean, // true
    val pageCount: Int, // 1
    val size: Int, // 20
    val total: Int // 10
) {
    data class Data(
        val author: String, // yetel
        val chapterId: Int, // 294
        val chapterName: String, // 完整项目
        val courseId: Int, // 13
        val desc: String, // EasyChat是一款开源的社交类的App。主要包含消息、好友、群组等相关的IM核心功能。部分界面参照了QQ、微信等相关社交APP。EasyChat APP采用MVVM模式构建。
        val envelopePic: String, // https://www.wanandroid.com/blogimgs/2efab4c3-4500-4980-ba31-0ca14098c6e0.png
        val id: Int, // 210058
        val link: String, // http://www.wanandroid.com/blog/show/2943
        val niceDate: String, // 2小时前
        val origin: String,
        val originId: Int, // 17387
        val publishTime: Long, // 1627614991000
        val title: String, // EasyChat是一款开源的社交类的App。主要包含消息、好友、群组等相关的IM核心功能
        val userId: Int, // 104421
        val visible: Int, // 0
        val zan: Int ,// 0
        var item_type: Int,
        var collect: Boolean=true  //设置无效
    ):MultiItemEntity {
        override fun getItemType(): Int {
            return item_type
        }
    }
}
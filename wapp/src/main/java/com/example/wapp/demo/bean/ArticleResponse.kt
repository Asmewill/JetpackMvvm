package com.example.wapp.demo.bean

data class ArticleResponse(
    val apkLink: String,
    val audit: Int, // 1
    val author: String, // xiaoyang
    val canEdit: Boolean, // false
    val chapterId: Int, // 440
    val chapterName: String, // 官方
    val collect: Boolean, // false
    val courseId: Int, // 13
    val desc: String, // <p>Gson大家一定不陌生，在很多项目中都大规模使用。</p><p>例如常见的：</p><pre><code>网络请求    -&gt;返回Json数据    -&gt;Gson解析为对象    -&gt;渲染页面</code></pre><p>很多时候，历史项目包含很多Gson解析对象在UI线程的操作，或者说即使在子线程其实也会影响页面展现速度。</p><p>大家都了解Gson对于对象的解析，如果不单独的配置TypeAdapter，那么其实内部是充满反射的。</p><p>问题来了：</p><p><strong>有没有什么低侵入的方案可以尽可能去除反射操作，从而提升运行效率？描述思路即可。</strong></p>
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean, // false
    val host: String,
    val id: Int, // 19623
    val link: String, // https://wanandroid.com/wenda/show/19623
    val niceDate: String, // 2021-08-30 21:36
    val niceShareDate: String, // 2021-08-30 21:36
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long, // 1630330608000
    val realSuperChapterId: Int, // 439
    val selfVisible: Int, // 0
    val shareDate: Long, // 1630330596000
    val shareUser: String,
    val superChapterId: Int, // 440
    val superChapterName: String, // 问答
    val tags: List<Tag>,
    val title: String, // 每日一问 | Gson中序列化对象的操作有低侵入的优化方案吗？
    val type: Int, // 1
    val userId: Int, // 2
    val visible: Int, // 1
    val zan: Int ,// 7
    var isTop:Boolean =false
) {
    data class Tag(
        val name: String, // 本站发布
        val url: String // /article/list/0?cid=440
    )
}
package com.example.oapp.bean

import java.io.Serializable

/**
 * Created by Owen on 2019/11/19
 */
class ToDoBean :Serializable{

    /**
     * curPage : 1
     * datas : [{"completeDateStr":"","content":"","date":1580400000000,"dateStr":"2020-01-31","id":17545,"priority":1,"status":0,"title":"我觉得环境","type":0,"userId":35130},{"completeDateStr":"","content":"阿娇的基督教","date":1577203200000,"dateStr":"2019-12-25","id":17542,"priority":1,"status":0,"title":"无和计算机","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1575043200000,"dateStr":"2019-11-30","id":17539,"priority":0,"status":0,"title":"大航海发货","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574784000000,"dateStr":"2019-11-27","id":17543,"priority":1,"status":0,"title":"我觉得很好的","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574784000000,"dateStr":"2019-11-27","id":17544,"priority":1,"status":0,"title":"啊结婚的角度看","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574524800000,"dateStr":"2019-11-24","id":17541,"priority":0,"status":0,"title":"我觉得很简单","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574438400000,"dateStr":"2019-11-23","id":17537,"priority":1,"status":0,"title":"啊结婚的角度看","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574179200000,"dateStr":"2019-11-20","id":17536,"priority":1,"status":0,"title":"以后","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17525,"priority":0,"status":0,"title":"无呵呵","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17526,"priority":0,"status":0,"title":"不是北戴河","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17527,"priority":0,"status":0,"title":"试过好多个","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17528,"priority":0,"status":0,"title":"找不到不断加剧","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17529,"priority":0,"status":0,"title":"是很多很多话","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17530,"priority":0,"status":0,"title":"独家","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17531,"priority":0,"status":0,"title":"你说的很多很多话","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17532,"priority":0,"status":0,"title":"啊结婚的话揭开了","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17533,"priority":0,"status":0,"title":"上海分行","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17534,"priority":0,"status":0,"title":"啊结婚的话附近","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17535,"priority":1,"status":0,"title":"五花肉恢复","type":0,"userId":35130},{"completeDateStr":"","content":"","date":1574092800000,"dateStr":"2019-11-19","id":17540,"priority":1,"status":0,"title":"啊看到经济法","type":0,"userId":35130}]
     * offset : 0
     * over : false
     * pageCount : 2
     * size : 20
     * total : 23
     */

    var curPage: Int = 0
    var offset: Int = 0
    var over: Boolean = false
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
    var datas: List<DatasBean>? = null

    class DatasBean :Serializable{
        /**
         * completeDateStr :
         * content :
         * date : 1580400000000
         * dateStr : 2020-01-31
         * id : 17545
         * priority : 1
         * status : 0
         * title : 我觉得环境
         * type : 0
         * userId : 35130
         */

        var completeDateStr: String? = null
        var content: String? = null
        var date: Long = 0
        var dateStr: String? = null
        var id: Int = 0
        var priority: Int = 0
        var status: Int = 0
        var title: String? = null
        var type: Int = 0
        var userId: Int = 0
    }
}

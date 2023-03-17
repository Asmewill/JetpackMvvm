package com.example.oapp.bean

import java.io.Serializable

/**
 * Created by jsxiaoshui on 2021/6/30
 */
class ProjectData : Serializable {
    /**
     * children : []
     * courseId : 13
     * id : 408
     * name : 鸿洋
     * order : 190000
     * parentChapterId : 407
     * userControlSetTop : false
     * visible : 1
     */
    var courseId = 0
    var id = 0
    var name: String? = null
    var order = 0
    var parentChapterId = 0
    var userControlSetTop = false
    var visible = 0
    var children: List<*>? = null
}
package com.example.oapp.utils

/**
 * Created by jsxiaoshui on 2021/8/5
 */
object PagePath {
    /**
     * 登录组件
     */
    object Login {
        private const val LOGIN = "/module_login"

        /**
         * 登录页
         */
        const val PAGER_LOGIN = LOGIN + "/Login"
    }


    /**
     * Main组件
     */
    object Main {
        private const val MAIN = "/module_main"

        /**
         * 主页面
         * path must be start with '/' and contain more than 2 '/'
         */
        const val PAGER_MAIN = MAIN + "/Mainsss"
    }

    /**
     * web 组件
     */
    object Web {
        const val WEB = "/module_web"
        const val PAGER_WEB = WEB + "/Web"
    }

    object Square {
        const val SQUARE = "/module_square"
        const val PAGER_SQUARE_LIST = SQUARE + "/square_list"
    }

}
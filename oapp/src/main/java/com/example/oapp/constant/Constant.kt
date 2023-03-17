package com.example.oapp.constant

/**
 * Created by jsxiaoshui on 2021/6/28
 */
object Constant {
    const val PASSWORD_KEY="password"
    const  val USER_NAME_KEY="user_name"
    const  val SEARCH_KEY="search_key"
    const val BASE_URL="https://www.wanandroid.com/"
    const val CONTENT_TITLE="title"
    const val CONTENT_URL="url"
    const val CONTENT_ID="id"
    const val TAB_CID="cid"
    const val PAGE_TYPE="page_type"
    const val IS_LOGIN="is_login"
    const val USER_INFO="user_info"
    const val LOGIN_BEAN="login_bean"
    const val ITEM_BENA="item_bean"
    const val EDIT_TYPE="edit_type"

    object Type{
        const val COLLECT_TYPE_KEY="collect_type_key"
        const val ABOUT_US_TYPE_KEY="about_us_type_key"
        const val SETTING_TYPE_KEY="setting_type_key"
        const val SEARCH_TYPE_KEY="search_type_key"
        const val ADD_TODO_TYPE_KEY="add_todo_type_key"
        const val SEE_TODO_TYPE_KEY="see_todo_type_key"
        const val EDIT_TODO_TYPE_KEY="edit_todo_type_key"
    }

    object PagePath{
        const val SPLASH="/page/SplashActivity"
        const val MAIN="/page/MainActivity"
        const val LOGIN="/page/LoginActivity"
        const val REGISTER="/page/RegisterActivity"

        const val CONTENT="/page/ContentActivity"
        const val SEARCH="/page/SearchActivity"
        const val COMMON="/page/CommonActivity"
        const val RANKLIST="/page/RankListActivity"

        const val SCORE="/page/ScoreActivity"
        const val TODO="/page/ToDoActivity"
        const val SETTING="/page/SettingActivity"
        const val KNOWLEDGE="/page/KnowledgeActivity"
        const val ERROR="/page/ErrorActivity"
    }
}




package com.example.oapp.mvi

/**
 * Created by Owen on 2025/5/14
 *
 * 穷举所有可能的用户操作
 */
sealed class HomeIntent {
    object  GetHomeList:HomeIntent()//加载第一页数据意图
    object  GetHomeListMore:HomeIntent()//上拉加载数据意图
    data class Search(val keyword: String) : HomeIntent() // 搜索意图

}

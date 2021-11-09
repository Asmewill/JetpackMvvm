package com.example.wapp.demo.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.NavigationResponse
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * Created by jsxiaoshui on 2021-11-09
 */
class NavigatorAdapter :BaseQuickAdapter<NavigationResponse,BaseViewHolder>(R.layout.item_system) {


    override fun convert(holder: BaseViewHolder, item: NavigationResponse) {
        val tv_system_title=holder.getView<TextView>(R.id.tv_system_title)
        val rv_system=holder.getView<RecyclerView>(R.id.rv_system)
        //使用RecycleView+flexboxLayoutManager做自动移动布局
        val flexboxLayout=FlexboxLayoutManager(context)
        flexboxLayout.flexDirection=FlexDirection.ROW  //行
        flexboxLayout.justifyContent=JustifyContent.FLEX_START// 左对齐
        rv_system.layoutManager=flexboxLayout
        //Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        rv_system.setHasFixedSize(true)
        rv_system.isNestedScrollingEnabled=false
        val navigatorChildAdapter=NavigatorChildAdapter()
        rv_system.adapter=navigatorChildAdapter
        navigatorChildAdapter.setList(item.articles)
        navigatorChildAdapter.setOnItemClickListener{adapter,view,positon->
            ToastUtils.showShort(adapter.getItem(positon).toString())
        }
        tv_system_title.text=item.name

    }
}
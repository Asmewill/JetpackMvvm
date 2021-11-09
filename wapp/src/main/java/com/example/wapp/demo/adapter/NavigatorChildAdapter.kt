package com.example.wapp.demo.adapter

import android.widget.TextView
import androidx.annotation.NavigationRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.NavigationResponse
import com.example.wapp.demo.bean.SystemResponse
import com.example.wapp.demo.utils.ColorUtil

/**
 * Created by jsxiaoshui on 2021-11-09
 */
class NavigatorChildAdapter:BaseQuickAdapter<NavigationResponse.Article,BaseViewHolder>(R.layout.flow_layout)  {

    override fun convert(holder: BaseViewHolder, item: NavigationResponse.Article) {
        val flow_tag=holder.getView<TextView>(R.id.flow_tag)
        flow_tag.text = item.title
        flow_tag.setTextColor(ColorUtil.randomColor())
    }
}
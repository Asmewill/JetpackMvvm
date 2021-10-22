package com.example.wapp.demo.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.SearchResponse
import com.example.wapp.demo.ext.setAdapterAnimation
import com.example.wapp.demo.utils.ColorUtil


/**
 * Created by jsxiaoshui on 2021-10-22
 */
class SearchHotAdapter:BaseQuickAdapter<SearchResponse, BaseViewHolder>(R.layout.flow_layout) {

    init {
        this.setAdapterAnimation(3)
    }
    override fun convert(holder: BaseViewHolder, item: SearchResponse) {
        val flow_tag=holder.getView<TextView>(R.id.flow_tag)
        flow_tag.setText(item.name)
        flow_tag.setTextColor(ColorUtil.randomColor())
    }
}
package com.example.wapp.demo.adapter

import android.text.TextUtils
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.UrlBean
import com.example.wapp.demo.ext.setAdapterAnimation
import com.example.wapp.demo.widget.CollectView


/**
 * Created by jsxiaoshui on 2021-10-22
 */
class CollectUrlAdapter():BaseQuickAdapter<UrlBean, BaseViewHolder>(R.layout.item_url) {
    init {
        this.setAdapterAnimation(2)
    }
    override fun convert(holder: BaseViewHolder, item: UrlBean) {
        val tv_title=holder.getView<TextView>(R.id.tv_title)
        val tv_url=holder.getView<TextView>(R.id.tv_url)
        val cv_collect=holder.getView<CollectView>(R.id.cv_collect)
        if(!TextUtils.isEmpty(item.name)){
            tv_title.setText(item.name)
        }
        if(!TextUtils.isEmpty(item.link)){
            tv_url.setText(item.link)
        }
        cv_collect.isChecked=true
    }
}
package com.example.oapp.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.KnowledgeData

/**
 * Created by jsxiaoshui on 2021/6/30
 */
 class KnowledgeAdapter:BaseQuickAdapter<KnowledgeData,BaseViewHolder>(R.layout.item_knowledge_tree_list) {


    override fun convert(holder: BaseViewHolder?, item: KnowledgeData?) {
        holder?:return
        item?:return
        val title_first=holder.getView<TextView>(R.id.title_first)
        val title_second=holder.getView<TextView>(R.id.title_second)

        title_first.setText(item.name)
        var secondTitle:String=""
        item.children?.forEach {
             secondTitle+=it.name+" "
        }
        title_second.setText(secondTitle)
    }
}
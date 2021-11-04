package com.example.wapp.demo.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.ext.toJson
import com.example.wapp.demo.ui.fragment.SearchFragment
import com.example.wapp.demo.utils.CacheUtil


/**
 * Created by jsxiaoshui on 2021-10-25
 */
class SearchHistoryAdapter(private val fragment:SearchFragment):BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_history) {


    override fun convert(holder: BaseViewHolder, item:String) {
        val item_history_text=holder.getView<TextView>(R.id.item_history_text)
        val item_history_del=holder.getView<ImageView>(R.id.item_history_del)
        item_history_text.setText(item)
        item_history_del.setOnClickListener{
            data.removeAt(holder.layoutPosition)
            notifyDataSetChanged()
            CacheUtil.setSearchHistoryData(data.toJson())
            if(data.isEmpty()){
                fragment.setClearTextVisibility(View.GONE)
            }
        }
    }
}
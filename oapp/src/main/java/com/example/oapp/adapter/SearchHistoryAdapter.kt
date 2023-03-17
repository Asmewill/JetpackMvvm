package com.example.oapp.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.SearchHistoryBean
import com.example.oapp.ui.SearchActivity

/**
 * Created by jsxiaoshui on 2021/7/9
 */
class SearchHistoryAdapter:BaseQuickAdapter<SearchHistoryBean,BaseViewHolder> (R.layout.item_search_history) {
    override fun convert(holder: BaseViewHolder?, item: SearchHistoryBean?) {
        holder?:return
        item?:return
        val tv_search_key=holder.getView<TextView>(R.id.tv_search_key)
        val iv_clear=holder.getView<ImageView>(R.id.iv_clear)
        item.key?.let {
            tv_search_key.text = it
        }
        iv_clear.setOnClickListener {
            if(mContext is SearchActivity){
                (mContext as SearchActivity).deleleHotKey(item.key!!)
            }
        }
    }
}
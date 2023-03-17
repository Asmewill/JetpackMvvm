package com.example.oapp.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.WeChatItemData
import com.example.oapp.ext.showToast
import com.example.oapp.viewmodel.CollectViewModel

/**
 * Created by jsxiaoshui on 2021/7/2
 */
class WeChatItemAdapter(private val mViewModel:CollectViewModel):BaseQuickAdapter<WeChatItemData.DatasBean,BaseViewHolder>(R.layout.item_wechat) {

    override fun convert(holder: BaseViewHolder?, item: WeChatItemData.DatasBean?) {
        holder?:return
        item?:return
        val tv_article_author=holder.getView<TextView>(R.id.tv_article_author)
        val tv_article_date=holder.getView<TextView>(R.id.tv_article_date)
        val tv_article_title=holder.getView<TextView>(R.id.tv_article_title)
        val tv_article_chapterName=holder.getView<TextView>(R.id.tv_article_chapterName)
        val iv_like=holder.getView<ImageView>(R.id.iv_like)
         item.author?.let {
             tv_article_author.setText(it)
         }
        item.title?.let {
            tv_article_title.setText(it)
        }
        item.chapterName?.let {
            tv_article_chapterName.setText(it)
        }
        item.niceDate?.let {
            tv_article_date.setText(it)
        }
        iv_like.setOnClickListener {
            showToast("collect。。。。。")
        }
        if(item.collect){
            iv_like.setImageResource(R.drawable.ic_like)
        }else{
            iv_like.setImageResource(R.drawable.ic_like_not)
        }
        iv_like.setOnClickListener {
            if(!item.collect){
                mViewModel.addCollect(item.id,holder.layoutPosition)
            }else{
                mViewModel.cancelCollect(item.id,holder.layoutPosition)
            }
        }

    }
}
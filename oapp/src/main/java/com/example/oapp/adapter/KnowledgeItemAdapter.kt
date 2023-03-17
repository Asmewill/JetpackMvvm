package com.example.oapp.adapter

import android.text.Html
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.KnowItemListBean
import com.example.oapp.bean.KnowledgeData
import com.example.oapp.viewmodel.CollectViewModel

/**
 * Created by jsxiaoshui on 2021/8/2
 */
class KnowledgeItemAdapter(private val mViewModel:CollectViewModel):BaseQuickAdapter<KnowItemListBean.Data,BaseViewHolder>(R.layout.item_knowledge_list) {
    override fun convert(holder: BaseViewHolder?, item: KnowItemListBean.Data?) {
        holder?:return
        item?:return
        var tv_article_author=holder.getView<TextView>(R.id.tv_article_author)
        var tv_article_date=holder.getView<TextView>(R.id.tv_article_date)
        var tv_article_title=holder.getView<TextView>(R.id.tv_article_title)
        var tv_article_chapterName=holder.getView<TextView>(R.id.tv_article_chapterName)
        var iv_like=holder.getView<ImageView>(R.id.iv_like)
        if(!TextUtils.isEmpty(item.author)){
            tv_article_author.text = item.author
        }else{
            tv_article_author.text = item.shareUser
        }
        tv_article_date.text = item.niceDate
        tv_article_title.text = Html.fromHtml(item.title).toString()
        tv_article_chapterName.text = item.superChapterName+"/"+item.chapterName
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
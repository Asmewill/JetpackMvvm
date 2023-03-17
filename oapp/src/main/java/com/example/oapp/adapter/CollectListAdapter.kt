package com.example.oapp.adapter

import android.text.Html
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.CollectArticle
import com.example.oapp.utils.ImageLoader
import com.example.oapp.viewmodel.CollectViewModel

/**
 * Created by jsxiaoshui on 2021/7/30
 */
class CollectListAdapter(private val mViewModel:CollectViewModel, list:List<CollectArticle.Data>):BaseMultiItemQuickAdapter<CollectArticle.Data,BaseViewHolder>(list) {
   //相当于在java的构造方法中去初始化
   init {
       addItemType(1,R.layout.item_collect_style_1)
       addItemType(2,R.layout.item_collect_style_2)
   }
    override fun convert(holder: BaseViewHolder?, item: CollectArticle.Data?) {
        holder?:return
        item?:return
        when(item.itemType){
            1->{
                var tv_article_author=holder.getView<TextView>(R.id.tv_article_author)
                var tv_article_date=holder.getView<TextView>(R.id.tv_article_date)
                var tv_article_title=holder.getView<TextView>(R.id.tv_article_title)
                var tv_article_chapterName=holder.getView<TextView>(R.id.tv_article_chapterName)
                var iv_like=holder.getView<ImageView>(R.id.iv_like)
                if(!TextUtils.isEmpty(item.author)){
                    tv_article_author.setText(item.author)
                }else{
                    tv_article_author.setText("无名英雄")
                }
                tv_article_date.text = item.niceDate
                tv_article_title.text = Html.fromHtml(item.title).toString()
                tv_article_chapterName.text = item.chapterName
                if(item.collect){
                    iv_like.setImageResource(R.drawable.ic_like)
                }else{
                    iv_like.setImageResource(R.drawable.ic_like_not)
                }
                iv_like.setOnClickListener {
                    if(!item.collect){
                      mViewModel.addCollect(item.originId,holder.layoutPosition)
                    }else{
                        mViewModel.cancelCollect(item.originId,holder.layoutPosition)
                    }
                }
            }
            2->{
                val item_project_list_iv=holder.getView<ImageView>(R.id.item_project_list_iv)
                val item_project_list_title_tv=holder.getView<TextView>(R.id.item_project_list_title_tv)
                val item_project_list_content_tv=holder.getView<TextView>(R.id.item_project_list_content_tv)
                val item_project_list_author_tv=holder.getView<TextView>(R.id.item_project_list_author_tv)
                val item_project_list_time_tv=holder.getView<TextView>(R.id.item_project_list_time_tv)
                val item_project_list_like_iv=holder.getView<ImageView>(R.id.item_project_list_like_iv)
                ImageLoader.loadIv(mContext,item.envelopePic!!,item_project_list_iv)
                item.title?.let {
                    item_project_list_title_tv.setText(it)
                }
                item.desc?.let {
                    item_project_list_content_tv.setText(it)
                }
                item.author?.let {
                    item_project_list_author_tv.setText(item.author)
                }
                item.niceDate?.let {
                    item_project_list_time_tv.setText(item.niceDate)

                }
                if(item.collect){
                    item_project_list_like_iv.setImageResource(R.drawable.ic_like)
                }else{
                    item_project_list_like_iv.setImageResource(R.drawable.ic_like_not)
                }
                item_project_list_like_iv.setOnClickListener {
                    if(!item.collect){
                        mViewModel.addCollect(item.originId,holder.layoutPosition)
                    }else{
                        mViewModel.cancelCollect(item.originId,holder.layoutPosition)
                    }
                }

            }
        }
    }
}
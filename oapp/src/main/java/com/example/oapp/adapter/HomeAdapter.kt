package com.example.oapp.adapter

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.HomeData
import com.example.oapp.utils.ImageLoader
import com.example.oapp.utils.SettingUtil
import com.example.oapp.viewmodel.CollectViewModel

/**
 * Created by jsxiaoshui on 2021/6/28
 */
class HomeAdapter(private val mViewModel:CollectViewModel):BaseQuickAdapter<HomeData.DatasBean,BaseViewHolder>(R.layout.item_home_list) {


    override fun convert(helper: BaseViewHolder?, item: HomeData.DatasBean?) {
        helper?:return
        item?:return
        var tv_article_top=helper.getView<TextView>(R.id.tv_article_top)
        var tv_article_fresh=helper.getView<TextView>(R.id.tv_article_fresh)
        var tv_article_tag=helper.getView<TextView>(R.id.tv_article_tag)
        var tv_article_author=helper.getView<TextView>(R.id.tv_article_author)
        var tv_article_date=helper.getView<TextView>(R.id.tv_article_date)
        var iv_article_thumbnail=helper.getView<ImageView>(R.id.iv_article_thumbnail)
        var tv_article_title=helper.getView<TextView>(R.id.tv_article_title)
        var tv_article_chapterName=helper.getView<TextView>(R.id.tv_article_chapterName)
        var iv_like=helper.getView<ImageView>(R.id.iv_like)
        if(item.isTop){
            tv_article_top.visibility= View.VISIBLE
        }else{
            tv_article_top.visibility= View.GONE
        }

        if(SettingUtil.getIsNoPhotoMode()){
            iv_article_thumbnail.visibility=View.GONE
        }else{
            iv_article_thumbnail.visibility=View.VISIBLE
            if(!TextUtils.isEmpty(item.envelopePic)){
                iv_article_thumbnail.visibility=View.VISIBLE
                ImageLoader.loadIv(mContext, item.envelopePic!!,iv_article_thumbnail)
            }else{
                iv_article_thumbnail.visibility=View.GONE
            }
        }
        if(item.fresh){
            tv_article_fresh.visibility=View.VISIBLE
        }else{
            tv_article_fresh.visibility=View.GONE
        }
        if(item.tags?.size!!>0){
            tv_article_tag?.setText(item.tags!!.get(0).name)
            tv_article_tag.visibility=View.VISIBLE
        }else{
            tv_article_tag.visibility=View.GONE
        }
        if(!TextUtils.isEmpty(item.author)){
            tv_article_author.setText(item.author)
        }else{
            tv_article_author.setText(item.shareUser)
        }
        tv_article_date.setText(item.niceDate)
        tv_article_title.setText(Html.fromHtml(item.title).toString())
        tv_article_chapterName.setText(item.superChapterName+"/"+item.chapterName)
        if(item.collect){
           iv_like.setImageResource(R.drawable.ic_like)
        }else{
            iv_like.setImageResource(R.drawable.ic_like_not)
        }
        iv_like.setOnClickListener {
            if(!item.collect){
                mViewModel.addCollect(item.id,helper.layoutPosition-1)
            }else{
                mViewModel.cancelCollect(item.id,helper.layoutPosition-1)
            }
        }
    }
}
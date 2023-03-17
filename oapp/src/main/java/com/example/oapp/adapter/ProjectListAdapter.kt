package com.example.oapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.ProjectItemData
import com.example.oapp.utils.ImageLoader
import com.example.oapp.utils.SettingUtil
import com.example.oapp.viewmodel.CollectViewModel

/**
 * Created by jsxiaoshui on 2021/7/5
 */
class ProjectListAdapter(private val mViewModel:CollectViewModel):BaseQuickAdapter<ProjectItemData.DatasBean,BaseViewHolder>(R.layout.item_project_list) {

    override fun convert(holder: BaseViewHolder?, itemdata: ProjectItemData.DatasBean?) {
        holder?:return
        itemdata?:return
        val item_project_list_iv=holder.getView<ImageView>(R.id.item_project_list_iv)
        val item_project_list_title_tv=holder.getView<TextView>(R.id.item_project_list_title_tv)
        val item_project_list_content_tv=holder.getView<TextView>(R.id.item_project_list_content_tv)
        val item_project_list_author_tv=holder.getView<TextView>(R.id.item_project_list_author_tv)
        val item_project_list_time_tv=holder.getView<TextView>(R.id.item_project_list_time_tv)
        val item_project_list_like_iv=holder.getView<ImageView>(R.id.item_project_list_like_iv)
        if(SettingUtil.getIsNoPhotoMode()){
            item_project_list_iv.visibility= View.GONE
        }else{
            item_project_list_iv.visibility= View.VISIBLE
        }
        ImageLoader.loadIv(mContext,itemdata.envelopePic!!,item_project_list_iv)
        itemdata.title?.let {
            item_project_list_title_tv.setText(it)
        }
        itemdata.desc?.let {
            item_project_list_content_tv.setText(it)
        }
        itemdata.author?.let {
            item_project_list_author_tv.setText(itemdata.author)
        }
        itemdata.niceDate?.let {
            item_project_list_time_tv.setText(itemdata.niceDate)
        }
        if(itemdata.collect){
            item_project_list_like_iv.setImageResource(R.drawable.ic_like)
        }else{
            item_project_list_like_iv.setImageResource(R.drawable.ic_like_not)
        }
        item_project_list_like_iv.setOnClickListener {
            if(!itemdata.collect){
                mViewModel.addCollect(itemdata.id,holder.layoutPosition)
            }else{
                mViewModel.cancelCollect(itemdata.id,holder.layoutPosition)
            }
        }
    }
}
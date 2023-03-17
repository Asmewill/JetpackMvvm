package com.example.oapp.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.ToDoBean
import com.example.oapp.constant.Constant
import com.example.oapp.ui.CommonActivity
import com.example.oapp.viewmodel.DoneViewModel
import com.example.oapp.viewmodel.EventViewModel
import com.example.oapp.widget.TitleTextView

/**
 * Created by jsxiaoshui on 2021/7/28
 */
class DoneAdapter(private val mViewModel:DoneViewModel):BaseQuickAdapter<ToDoBean.DatasBean,BaseViewHolder>(R.layout.item_todo_list) {

    override fun convert(holder: BaseViewHolder?, item: ToDoBean.DatasBean?) {
        holder?:return
        item?:return
        val tv_header=holder.getView<TextView>(R.id.tv_header)
        val tv_tilt=holder.getView<TitleTextView>(R.id.tv_tilt)
        val tv_todo_title=holder.getView<TextView>(R.id.tv_todo_title)
        val tv_todo_desc=holder.getView<TextView>(R.id.tv_todo_desc)
        val btn_done=holder.getView<Button>(R.id.btn_done)
        val btn_delete=holder.getView<Button>(R.id.btn_delete)
        val item_todo_content=holder.getView<RelativeLayout>(R.id.item_todo_content)
        btn_done.text="复原"
        item.dateStr?.let {
            tv_header.text=it
        }
        if(item.priority==1){
            tv_tilt.visibility= View.VISIBLE
        }else{
            tv_tilt.visibility= View.GONE
        }
        item.title?.run {
            tv_todo_title.text=this
        }
        tv_todo_desc.text=item.content
        btn_done.setOnClickListener(MyOnClickListener(holder.layoutPosition,this,mContext,0,mViewModel,item))
        btn_delete.setOnClickListener(MyOnClickListener(holder.layoutPosition,this,mContext,1,mViewModel,item))
        item_todo_content.setOnClickListener(MyOnClickListener(holder.layoutPosition,this,mContext,2,mViewModel,item))
    }

    private class MyOnClickListener(private val position:Int,private val adatepr:BaseQuickAdapter<ToDoBean.DatasBean,
            BaseViewHolder>,private val mContext:Context,private val type:Int,private val mViewModel:DoneViewModel,private val  item: ToDoBean.DatasBean) :View.OnClickListener{

        override fun onClick(v: View?) {
            when(type){
                0->{
                    mViewModel.resume("done",item.id,0)
                    adatepr.remove(position)
                }
                1->{
                    mViewModel.delete("delete",item.id)
                    adatepr.remove(position)
                }
                2->{
                    ARouter.getInstance().build(Constant.PagePath.COMMON)
                        .withString(Constant.PAGE_TYPE,Constant.Type.SEE_TODO_TYPE_KEY)
                        .withSerializable(Constant.ITEM_BENA,item)
                        .navigation()
                }
            }
        }
    }

}
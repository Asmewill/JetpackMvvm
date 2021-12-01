package com.example.wapp.demo.adapter

import android.widget.Button
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.SearchResponse
import com.example.wapp.demo.ext.setAdapterAnimation
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.utils.ColorUtil
import com.example.wapp.demo.viewmodel.ConversationViewModel


/**
 * Created by jsxiaoshui on 2021-10-22
 */
class AddContackAdapter(private val conversationViewModel:ConversationViewModel):BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_add_contact) {

    init {
        this.setAdapterAnimation(2)
    }
    override fun convert(holder: BaseViewHolder, item: String) {
        val tv_search_name=holder.getView<TextView>(R.id.tv_search_name)
        tv_search_name.setText(item)
        val btn_search_add=holder.getView<Button>(R.id.btn_search_add)
        btn_search_add.setOnClickListener {
            val userName= CacheUtil.getUser()?.username
            if(userName.equals(item)){
                ToastUtils.showShort("不能添加自己为好友")
            }else{
                ToastUtils.showShort("去添加好友")
                conversationViewModel.aysncAddContact(item,"加个好友呗")
            }
        }
    }
}
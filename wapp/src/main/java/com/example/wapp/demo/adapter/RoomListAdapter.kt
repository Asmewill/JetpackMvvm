package com.example.wapp.demo.adapter

import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.room.AppDataBase
import com.example.wapp.demo.room.User


/**
 * Data :2023/3/13
 * Time:18:38
 * Author:shuij
 *
 */
class RoomListAdapter:BaseQuickAdapter<User,BaseViewHolder>(R.layout.item_room) {

    override fun convert(holder: BaseViewHolder, item: User) {
        var tv_num=holder.getView<TextView>(R.id.tv_num)
        var tv_name=holder.getView<TextView>(R.id.tv_name)
        var tv_age=holder.getView<TextView>(R.id.tv_age)
        var btn_delete=holder.getView<Button>(R.id.btn_delete)
        var btn_edit=holder.getView<Button>(R.id.btn_edit)
        var tv_xueli=holder.getView<TextView>(R.id.tv_xueli)
        tv_num.text = item.id.toString()
        tv_name.setText(item.name)
        tv_age.setText("年龄："+item.age)
        tv_xueli.text=item.xueli
        btn_delete.setOnClickListener {
            AppDataBase.getInstance().userDao().deleteUser(item)
           var userList= AppDataBase.getInstance().userDao().getUsers().toMutableList()
            setNewInstance(userList)
        }
        btn_edit.setOnClickListener {
            item.name=item.name+"_e"
            AppDataBase.getInstance().userDao().updateUser(item)
            setNewInstance(AppDataBase.getInstance().userDao().getUsers().toMutableList())
        }
    }
}
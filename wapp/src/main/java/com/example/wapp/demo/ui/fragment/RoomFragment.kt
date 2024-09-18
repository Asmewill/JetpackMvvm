package com.example.wapp.demo.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentRoomBinding
import com.example.wapp.demo.adapter.RoomListAdapter
import com.example.wapp.demo.bean.UserInfo
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.room.AppDataBase
import com.example.wapp.demo.room.User
import com.example.wapp.demo.viewmodel.DemoViewModel
import kotlinx.android.synthetic.main.fragment_room.*

/**
 * Data :2023/3/13
 * Time:18:30
 * Author:shuij
 *
 */
class RoomFragment:BaseVmDbFragment<DemoViewModel,FragmentRoomBinding>() {
    private var i:Int=-1

    private val roomListAdapter by lazy {
        RoomListAdapter()
    }

    override fun layoutId(): Int {
        return R.layout.fragment_room
    }

    override fun initView() {
        toolbar.initClose(titleStr = "Room数据库demo") {
            nav().navigateUp()
        }
        rv_content.layoutManager=LinearLayoutManager(mActivity)
        rv_content.adapter=roomListAdapter
        roomListAdapter.setNewInstance(AppDataBase.getInstance().userDao().getUsers().toMutableList())
        btn_added.setOnClickListener {
            addItem()
        }
        btn_delete_all.setOnClickListener {
            AppDataBase.getInstance().userDao().deleteUsers(roomListAdapter.data.toList())
           var userList= AppDataBase.getInstance().userDao().getUsers().toMutableList()
            roomListAdapter.setNewInstance(userList)
        }

    }

    override fun initData() {

    }

    fun addItem(){
        i++
        AppDataBase.getInstance().userDao().addUser(User("Tim_$i",30+i,"男","高中"))
        var userList=AppDataBase.getInstance().userDao().getUsers()
        roomListAdapter.setNewInstance(userList.toMutableList())
    }

    override fun createObserver() {

    }
}
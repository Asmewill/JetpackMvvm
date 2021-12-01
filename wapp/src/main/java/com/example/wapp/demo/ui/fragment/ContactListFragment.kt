package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.blankj.utilcode.util.ToastUtils
import com.example.wapp.R
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.viewmodel.ConversationViewModel
import com.hyphenate.easeui.constants.EaseConstant
import com.hyphenate.easeui.domain.EaseUser
import com.hyphenate.easeui.model.EaseEvent
import com.hyphenate.easeui.modules.contact.EaseContactListFragment
import com.hyphenate.easeui.utils.EaseCommonUtils

/**
 * Created by jsxiaoshui on 2021-11-23
 * 联系人列表
 */
class ContactListFragment : EaseContactListFragment() {

    private var tempList= arrayListOf<EaseUser>()
    private val conversationViewModel: ConversationViewModel by viewModels()

    var tvSearch: AppCompatTextView? = null

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        addSearchView()
        addHeader()
    }

    override fun initData() {
        super.initData()
        conversationViewModel.contactLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isSuccess&&it.listData.size>0){
                tempList=it.listData
                contactLayout.contactList.setData(it.listData)
            }else{
                ToastUtils.showShort(it.errorMsg)
            }
        })
        LiveDataBus.get()
            .with(DemoConstant.CONTACT_CHANGE, EaseEvent::class.java).observe(viewLifecycleOwner,
                Observer {
                    //请求服务器联系人列表
                    conversationViewModel.getContactList()
                }
       )
       //请求服务器联系人列表
       conversationViewModel.getContactList()
    }


    override fun initListener() {
        super.initListener()
        contactLayout.swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    contactLayout.swipeRefreshLayout.isRefreshing = false
                    //请求服务器联系人列表
                    conversationViewModel.getContactList()
                }
            }, 2000)

        }
        contactLayout.contactList.setOnCustomItemClickListener { view, position ->
            val item = contactLayout.contactList.customAdapter.getItem(position)
            when (item.id) {
                R.id.contact_header_item_new_chat -> {
                    nav().navigate(R.id.action_Main_to_AddContactFragment)
                }
                R.id.contact_header_item_group_list -> {
                    ToastUtils.showShort("群聊")
                }
                R.id.contact_header_item_chat_room_list -> {
                    ToastUtils.showShort("聊天室")
                }
            }
        }
    }

    private fun addSearchView() {
        //添加搜索会话布局
        val view = LayoutInflater.from(mContext).inflate(R.layout.demo_layout_search, null)
        llRoot.addView(view, 0)
        tvSearch = view.findViewById<AppCompatTextView>(R.id.tv_search)
        tvSearch?.let {
            it.hint = "搜索"
        }
    }

    /**
     * 添加头布局
     */
    fun addHeader() {
        contactLayout.contactList.addCustomItem(
            R.id.contact_header_item_new_chat,
            R.mipmap.em_friends_new_chat,
            "新的好友"
        )
        contactLayout.contactList.addCustomItem(
            R.id.contact_header_item_group_list,
            R.mipmap.em_friends_group_chat,
            "群聊"
        )
        contactLayout.contactList.addCustomItem(
            R.id.contact_header_item_chat_room_list,
            R.mipmap.em_friends_chat_room,
            "聊天室"
        )
    }

    override fun onItemClick(view: View?, position: Int) {
        super.onItemClick(view, position)
        var item=tempList[position]
        nav().navigate(R.id.action_Main_to_ChatFragment,Bundle().apply {
            this.putString(Constant.conversationId,item.username)
            this.putInt(Constant.chatType, EaseConstant.CHATTYPE_SINGLE)
        })
    }


}
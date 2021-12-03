package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.example.wapp.R
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.viewmodel.ConversationViewModel
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.manager.EaseSystemMsgManager
import com.hyphenate.easeui.model.EaseEvent
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment
import com.hyphenate.easeui.utils.EaseCommonUtils

/**
 * Created by jsxiaoshui on 2021-11-23
 * 会话
 */
class ConversationListFragment: EaseConversationListFragment() {

    private val conversationViewModel: ConversationViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //添加空布局
        conversationListLayout.listAdapter.emptyLayoutId = R.layout.ease_layout_default_no_data
        //从服务器获取消息后回调处理UI
        conversationViewModel.conversationLiveData.observe(viewLifecycleOwner, Observer {
           if(it.isSuccess){
               conversationListLayout.setData(it.listData)
               conversationListLayout.loadDefaultData()
           }else{
              ToastUtils.showShort(it.errorMsg)
           }
        })

        conversationListLayout.showItemDefaultMenu(true)

        /****
         * ChatPresenter中进行各种监听
         *
         */
        //进入聊天详情之后，刷新列表，更新消息个数
        LiveDataBus.get().with(DemoConstant.CONVERSATION_READ, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })

        //收到好友邀请之后监听，刷新列表，显示系统消息
        LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })

        LiveDataBus.get().with(DemoConstant.NOTIFY_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner,Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.GROUP_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.CHAT_ROOM_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.CONVERSATION_DELETE, EaseEvent::class.java).observe(
            viewLifecycleOwner,Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.CONVERSATION_READ, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.CONTACT_ADD, EaseEvent::class.java).observe(
            viewLifecycleOwner,Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.CONTACT_UPDATE, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.MESSAGE_CALL_SAVE, Boolean::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        LiveDataBus.get().with(DemoConstant.MESSAGE_NOT_SEND, Boolean::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
    }
    override fun initData() {
        super.initData()
        conversationViewModel.fetchConversationsFromServer()
    }

    override fun initListener() {
        super.initListener()
    }

    /**
     * 重写onItemClick
     * item点击事件
     */

    override fun onItemClick(view: View?, position: Int) {
        super.onItemClick(view, position)
        val item = conversationListLayout.getItem(position).info
        if (item is EMConversation) {
            if (EaseSystemMsgManager.getInstance().isSystemConversation(item)) {
                 nav().navigate(R.id.action_Main_to_SystemMsgFragment)
            } else {
                nav().navigate(R.id.action_Main_to_ChatFragment,Bundle().apply {
                    this.putString(Constant.conversationId,item.conversationId())
                    this.putInt(Constant.chatType,EaseCommonUtils.getChatType(item))
                })
            }
        }

    }
    /**
     * 重写下拉刷新方法
     */
    override fun onRefresh() {
       // super.onRefresh()
       //从服务器获取最新的消息，不包含哪些存在本地数据库的消息【比如好友邀请提醒】
       conversationViewModel.fetchConversationsFromServer()

    }
}
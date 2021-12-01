package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.example.wapp.R
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.hxchat.InviteMessageStatus
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.hxchat.PushAndMessageHelper
import com.example.wapp.demo.viewmodel.ConversationViewModel
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.manager.EaseSystemMsgManager
import com.hyphenate.easeui.model.EaseEvent
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment
import com.hyphenate.easeui.utils.EaseCommonUtils
import com.hyphenate.util.EMLog

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
        //添加联系人监听
        EMClient.getInstance().contactManager().setContactListener(ChatContactListener())
        //收到好友邀请之后监听
        LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner, Observer {
                conversationListLayout.loadDefaultData()
            })
        //设置长按显示默认的对话框，默认为true
        conversationListLayout.showItemDefaultMenu(true)
    }

    private inner class ChatContactListener : EMContactListener {
        override fun onContactAdded(username: String) {

        }

        override fun onContactDeleted(username: String) {

        }

        override fun onContactInvited(username: String, reason: String) {
            EMLog.i("ChatContactListener", "onContactInvited")
            val allMessages = EaseSystemMsgManager.getInstance().allMessages
            if (allMessages != null && !allMessages.isEmpty()) {
                for (message in allMessages) {
                    val ext = message.ext()
                    if (ext != null && !ext.containsKey(DemoConstant.SYSTEM_MESSAGE_GROUP_ID)
                        && ext.containsKey(DemoConstant.SYSTEM_MESSAGE_FROM) && TextUtils.equals(
                            username,
                            ext[DemoConstant.SYSTEM_MESSAGE_FROM] as String?
                        )
                    ) {
                        EaseSystemMsgManager.getInstance().removeMessage(message)
                    }
                }
            }
            val ext = EaseSystemMsgManager.getInstance().createMsgExt()
            ext[DemoConstant.SYSTEM_MESSAGE_FROM] = username
            ext[DemoConstant.SYSTEM_MESSAGE_REASON] = reason
            ext[DemoConstant.SYSTEM_MESSAGE_STATUS] = InviteMessageStatus.BEINVITEED.name
            //这是是将好友邀请信息保存到本地数据库
            val message = EaseSystemMsgManager.getInstance()
                .createMessage(PushAndMessageHelper.getSystemMessage(ext), ext)
            notifyNewInviteMessage(message)
            val event = EaseEvent.create(DemoConstant.CONTACT_CHANGE, EaseEvent.TYPE.CONTACT)
            LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE).postValue(event)
            ToastUtils.showShort(MyApp.instance.getString(InviteMessageStatus.BEINVITEED.getMsgContent(), username))
         //   showToast(context.getString(InviteMessageStatus.BEINVITEED.getMsgContent(), username))

        }

        override fun onFriendRequestAccepted(username: String) {

        }

        override fun onFriendRequestDeclined(username: String) {

        }
    }


     fun notifyNewInviteMessage(msg: EMMessage) {
        // notify there is new message
        EaseIM.getInstance().notifier.vibrateAndPlayTone(null)
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
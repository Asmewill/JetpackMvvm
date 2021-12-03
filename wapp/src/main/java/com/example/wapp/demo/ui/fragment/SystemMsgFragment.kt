package com.example.wapp.demo.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSystemMsgsBinding
import com.example.wapp.demo.adapter.SystemMsgAdapter
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.viewmodel.ConversationViewModel
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.model.EaseEvent
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_system_msgs.*

/**
 * Created by jsxiaoshui on 2021-11-26
 */
class SystemMsgFragment:BaseVmDbFragment<ConversationViewModel,FragmentSystemMsgsBinding>() {
    private val conversationViewModel:ConversationViewModel by viewModels()

    private val  systemMsgAdapter  by lazy {
        SystemMsgAdapter(mutableListOf(),conversationViewModel)
    }
    override fun layoutId(): Int {
        return  R.layout.fragment_system_msgs
    }

    override fun initView() {
        tv_title.text="系统消息"
        titlebar.setNavigationIcon(R.drawable.ic_back)
        titlebar.setNavigationOnClickListener{
            nav().navigateUp()
        }
        rv_list.layoutManager=LinearLayoutManager(mActivity)
        rv_list.adapter=systemMsgAdapter
        //通知会话列表刷新---更新消息个数
        LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE)
            .postValue(EaseEvent(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.TYPE.MESSAGE))

    }

    override fun initData() {
        conversationViewModel.loadSystemMsg(20)
        //所有系统消息设置为已读
        makeAllMsgRead()
    }

    fun makeAllMsgRead() {
        val conversation = EMClient.getInstance().chatManager().getConversation(
            DemoConstant.DEFAULT_SYSTEM_MESSAGE_ID,
            EMConversation.EMConversationType.Chat,
            true)
       conversation.markAllMessagesAsRead()
       LiveDataBus.get().with(DemoConstant.NOTIFY_CHANGE)
            .postValue(EaseEvent.create(DemoConstant.NOTIFY_CHANGE, EaseEvent.TYPE.NOTIFY))
    }

    override fun createObserver() {
        conversationViewModel.systemLiveData.observe(mActivity, Observer {
                systemMsgAdapter.setList(it.listData)
            }
        )
        conversationViewModel.agreeLiveData.observe(mActivity, Observer {
            if(it.isSuccess){
                conversationViewModel.loadSystemMsg(100)
                //通知联系人列表
                val event = EaseEvent.create(DemoConstant.CONTACT_CHANGE, EaseEvent.TYPE.CONTACT)
                LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE).postValue(event)
            }else{
                ToastUtils.showShort(it.errorMsg)
            }
        })

        conversationViewModel.refuseLiveData.observe(mActivity,Observer{
            conversationViewModel.loadSystemMsg(100)
            //通知联系人列表
            val event = EaseEvent.create(DemoConstant.CONTACT_CHANGE, EaseEvent.TYPE.CONTACT)
            LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE).postValue(event)
        })
    }
}
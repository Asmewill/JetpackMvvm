package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import com.blankj.utilcode.util.KeyboardUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentChatBinding
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.viewmodel.ConversationViewModel
import com.hyphenate.easeui.constants.EaseConstant
import com.hyphenate.easeui.model.EaseEvent
import kotlinx.android.synthetic.main.fragment_system_msgs.*

/**
 * Created by jsxiaoshui on 2021-11-29
 */
class ChatFragment:BaseVmDbFragment<ConversationViewModel,FragmentChatBinding>() {
    private lateinit var fragment: ChatChildFragment
    private var conversionId=""
    private var chatType:Int=-1
    private var historyMsgId=""
    override fun layoutId(): Int {
        return R.layout.fragment_chat
    }

    override fun initView() {
        chatType= arguments?.getInt(Constant.chatType)!!
        conversionId= arguments?.getString(Constant.conversationId)!!
      //  historyMsgId=arguments?.getString(Constant.HISTORY_MSG_ID)!!
        tv_title.text=conversionId
        titlebar.setNavigationIcon(R.drawable.ic_back)
        titlebar.setNavigationOnClickListener{
            KeyboardUtils.hideSoftInput(requireActivity())
            nav().navigateUp()

        }
        initChatFragment()



    }

    private fun initChatFragment() {
        fragment = ChatChildFragment()
        val bundle = Bundle()
        bundle.putString(EaseConstant.EXTRA_CONVERSATION_ID, conversionId)
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType)
      //  bundle.putString(DemoConstant.HISTORY_MSG_ID, historyMsgId)
        bundle.putBoolean(EaseConstant.EXTRA_IS_ROAM,false)
        fragment!!.arguments = bundle
        mActivity.supportFragmentManager.beginTransaction().replace(R.id.fl_fragment, fragment!!, "chat").commit()
    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
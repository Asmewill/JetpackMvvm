package com.example.wapp.demo.ui.fragment

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.wapp.R
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.hxchat.event.VideoEvent
import com.hyphenate.easeui.model.EaseEvent
import com.hyphenate.easeui.modules.chat.EaseChatFragment
import com.hyphenate.util.EMFileHelper
import com.hyphenate.util.EMLog

/**
 * Created by jsxiaoshui on 2021-11-30
 */
class ChatChildFragment:EaseChatFragment() {

    override fun initView() {
        super.initView()
        LiveDataBus.get().with(Constant.CHOOSE_VIDEO, VideoEvent::class.java).observe(viewLifecycleOwner, Observer {
            if (it.intent != null) {
                val duration = it.intent.getIntExtra("dur", 0)
                val videoPath = it.intent.getStringExtra("path")
                val uriString = it.intent.getStringExtra("uri")
                EMLog.d("ChatChildFragment", "path = $videoPath uriString = $uriString")
                if (!TextUtils.isEmpty(videoPath)) {
                    chatLayout.sendVideoMessage(Uri.parse(videoPath), duration)
                } else {
                    val videoUri = EMFileHelper.getInstance().formatInUri(uriString)
                    chatLayout.sendVideoMessage(videoUri, duration)
                }
            }
        })
       //通知会话列表刷新---更新消息个数
        LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE)
            .postValue(EaseEvent(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.TYPE.MESSAGE))


        /****
         * 收到各种消息之后进行处理
         */
       /* LiveDataBus.get().with(DemoConstant.MESSAGE_CALL_SAVE, Boolean::class.java).observe(
            viewLifecycleOwner
        ) { event ->
            if (event == null) {
                return@observe
            }
            if (event) {
                chatLayout.chatMessageListLayout.refreshToLatest()
            }
        }

        LiveDataBus.get().with(DemoConstant.CONVERSATION_DELETE, EaseEvent::class.java).observe(
            viewLifecycleOwner
        ) { event ->
            if (event == null) {
                return@observe
            }
            if (event.isMessageChange) {
                chatLayout.chatMessageListLayout.refreshMessages()
            }
        }*/

       /* LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent::class.java).observe(
            viewLifecycleOwner
        ) { event ->
            if (event == null) {
                return@observe
            }
            if (event.isMessageChange) {
                chatLayout.chatMessageListLayout.refreshToLatest()
            }
        }*/
       /* LiveDataBus.get().with(DemoConstant.CONVERSATION_READ, EaseEvent::class.java).observe(
            viewLifecycleOwner
        ) { event ->
            if (event == null) {
                return@observe
            }
            if (event.isMessageChange) {
                chatLayout.chatMessageListLayout.refreshMessages()
            }
        }

        //更新用户属性刷新列表

        //更新用户属性刷新列表
        LiveDataBus.get().with(DemoConstant.CONTACT_ADD, EaseEvent::class.java)
            .observe(viewLifecycleOwner) { event ->
                if (event == null) {
                    return@observe
                }
                if (event != null) {
                    chatLayout.chatMessageListLayout.refreshMessages()
                }
            }

        LiveDataBus.get().with(DemoConstant.CONTACT_UPDATE, EaseEvent::class.java).observe(
            viewLifecycleOwner
        ) { event ->
            if (event == null) {
                return@observe
            }
            if (event != null) {
                chatLayout.chatMessageListLayout.refreshMessages()
            }
        }*/
    }

    override fun initListener() {
        super.initListener()
    }

    override fun initData() {
        super.initData()
    }


    /***
     * 视频菜单点击事件
     */
    override fun selectVideoFromLocal() {
        super.selectVideoFromLocal()
        nav().navigate(R.id.action_Chat_to_ImageGridFragment)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            val duration = data.getIntExtra("dur", 0)
            val videoPath = data.getStringExtra("path")
            val uriString = data.getStringExtra("uri")
            EMLog.d("ChatChildFragment", "path = $videoPath uriString = $uriString")
            if (!TextUtils.isEmpty(videoPath)) {
                chatLayout.sendVideoMessage(Uri.parse(videoPath), duration)
            } else {
                val videoUri = EMFileHelper.getInstance().formatInUri(uriString)
                chatLayout.sendVideoMessage(videoUri, duration)
            }
        }
    }




}
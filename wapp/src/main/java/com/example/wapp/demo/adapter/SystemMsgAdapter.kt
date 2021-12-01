package com.example.wapp.demo.adapter

import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.hxchat.InviteMessageStatus
import com.example.wapp.demo.viewmodel.ConversationViewModel
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.utils.EaseDateUtils
import com.hyphenate.easeui.widget.EaseImageView
import com.hyphenate.exceptions.HyphenateException
import java.util.*

/**
 * Created by jsxiaoshui on 2021-10-11
 */
class SystemMsgAdapter(data: MutableList<EMMessage>,private val conversationViewModel:ConversationViewModel) :
    BaseDelegateMultiAdapter<EMMessage, BaseViewHolder>(data) {

    init {
        //第一步---设置ItemType
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<EMMessage>() {
            override fun getItemType(data: List<EMMessage>, position: Int): Int {
                var statusParams: String? = null
                try {
                    statusParams =
                        data.get(position).getStringAttribute(DemoConstant.SYSTEM_MESSAGE_STATUS)
                } catch (e: HyphenateException) {
                    e.printStackTrace()
                }
                val status: InviteMessageStatus = InviteMessageStatus.valueOf(statusParams!!)

                if (status == InviteMessageStatus.BEINVITEED  //个人
                    || status == InviteMessageStatus.BEAPPLYED //群组
                    || status == InviteMessageStatus.GROUPINVITATION //群组
                ){
                    return 1  //被邀请
                }else{
                    return 2 //同意或者拒绝邀请
                }
            }
        })
        //第二步 绑定item类型
        getMultiTypeDelegate()?.addItemType(1, R.layout.item_invite_msg_invite)
        getMultiTypeDelegate()?.addItemType(2, R.layout.item_invite_msg_agree)
    }

    override fun convert(holder: BaseViewHolder, msg: EMMessage) {
        when (holder.itemViewType) {
            1 -> {//文章
                var name: TextView? = null
                var message: TextView? = null
                var agree: Button? = null
                var refuse: Button? = null
                var avatar: EaseImageView? = null
                var time: TextView? = null
                name = holder.getView<TextView>(R.id.name)
                message = holder.getView<TextView>(R.id.message)
                agree = holder.getView<Button>(R.id.agree)
                refuse = holder.getView<Button>(R.id.refuse)
                time = holder.getView<TextView>(R.id.time)
                avatar = holder.getView<EaseImageView>(R.id.avatar)
//                avatar.setShapeType(
//                    EaseIM.getInstance().avatarOptions.avatarShape
//                )

                var reason: String? = null
                try {
                    name.setText(msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM))
                    reason = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_REASON)
                } catch (e: HyphenateException) {
                    e.printStackTrace()
                }
                if (TextUtils.isEmpty(reason)) {
                    var statusParams: String? = null
                    try {
                        statusParams = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_STATUS)
                        val status = InviteMessageStatus.valueOf(statusParams)
                        if (status === InviteMessageStatus.BEINVITEED) {
                            reason = name!!.context.getString(
                                InviteMessageStatus.BEINVITEED.msgContent,
                                msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM)
                            )
                        } else if (status === InviteMessageStatus.BEAPPLYED) { //application to join group
                            reason = name!!.context.getString(
                                InviteMessageStatus.BEAPPLYED.msgContent,
                                msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM),
                                msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_NAME)
                            )
                        } else if (status === InviteMessageStatus.GROUPINVITATION) {
                            reason = name!!.context.getString(
                                InviteMessageStatus.GROUPINVITATION.msgContent,
                                msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_INVITER),
                                msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_NAME)
                            )
                        }
                    } catch (e: HyphenateException) {
                        e.printStackTrace()
                    }
                }
                message!!.text = reason
                time!!.text =
                    EaseDateUtils.getTimestampString(context, Date(msg.getMsgTime()))

                agree!!.setOnClickListener { view: View? ->
                    ToastUtils.showShort("agree")
                    conversationViewModel.agreeInvite(msg)
                }

                refuse!!.setOnClickListener { view: View? ->
                    conversationViewModel.refuseInvite(msg)
                }
            }
            2 -> {
                var name: TextView? = null
                var message: TextView? = null
                var avatar: EaseImageView? = null
                var time: TextView? = null

                name = holder.getView<TextView>(R.id.name)
                message = holder.getView<TextView>(R.id.message)
                avatar = holder.getView<EaseImageView>(R.id.avatar)
                time = holder.getView<TextView>(R.id.time)
//                avatar.setShapeType(
//                    EaseIM.getInstance().avatarOptions.avatarShape
//                )
                var reason: String? = null
                try {
                    name.text = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM)
                    reason = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_REASON)
                } catch (e: HyphenateException) {
                    e.printStackTrace()
                }
                if (TextUtils.isEmpty(reason)) {
                    var statusParams: String? = null
                    try {
                        statusParams = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_STATUS)
                        val status = InviteMessageStatus.valueOf(statusParams)
                        if (status === InviteMessageStatus.AGREED) {
                            reason = name.context.getString(
                                InviteMessageStatus.AGREED.msgContent,
                                msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM)
                            )
                        } else if (status === InviteMessageStatus.BEAGREED) {
                            reason = name.context.getString(InviteMessageStatus.BEAGREED.msgContent)
                        }
                    } catch (e: HyphenateException) {
                        e.printStackTrace()
                    }
                }
                message.text = reason
                time.text =
                    EaseDateUtils.getTimestampString(context, Date(msg.msgTime))

            }
        }
    }
}
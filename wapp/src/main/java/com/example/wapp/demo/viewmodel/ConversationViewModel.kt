package com.example.wapp.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.example.oapp.base.BaseViewModel
import com.example.wapp.R
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.bean.ListDataUiState
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.hxchat.InviteMessageStatus
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.hxchat.entity.EmUserEntity
import com.hyphenate.EMCallBack
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.easeui.constants.EaseConstant
import com.hyphenate.easeui.domain.EaseUser
import com.hyphenate.easeui.manager.EaseSystemMsgManager
import com.hyphenate.easeui.manager.EaseThreadManager
import com.hyphenate.easeui.model.EaseEvent
import com.hyphenate.easeui.modules.conversation.model.EaseConversationInfo
import com.hyphenate.exceptions.HyphenateException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by jsxiaoshui on 2021-11-23
 */
class ConversationViewModel:BaseViewModel() {

    var conversationLiveData: MutableLiveData<ListDataUiState<EaseConversationInfo>> = MutableLiveData()
    var addContactLiveData: MutableLiveData<ListDataUiState<String>> = MutableLiveData()
    var systemLiveData: MutableLiveData<ListDataUiState<EMMessage>> = MutableLiveData()
    var agreeLiveData: MutableLiveData<ListDataUiState<String>> = MutableLiveData()
    var refuseLiveData: MutableLiveData<ListDataUiState<String>> = MutableLiveData()

    /***
     * 会话列表拉取最新消息，不包括系统消息
     */
    fun fetchConversationsFromServer(){
        requestHX(
            block = {
                EMClient.getInstance().chatManager().asyncFetchConversationsFromServer(object :
                    EMValueCallBack<Map<String?, EMConversation>> {
                    override fun onSuccess(value: Map<String?, EMConversation>) {
                        val conversations: List<EMConversation> = ArrayList(value.values)
                        val infoList: ArrayList<EaseConversationInfo> = arrayListOf()
                        if (!conversations.isEmpty()) {
                            var info: EaseConversationInfo? = null
                            for (conversation in conversations) {
                                info = EaseConversationInfo()
                                info.info = conversation
                                if(conversation.lastMessage!=null){
                                    info.timestamp = conversation.lastMessage.msgTime
                                }
                                infoList.add(info)
                            }
                        }
                        val listDataUiState=ListDataUiState(
                            isSuccess = true,
                            listData = infoList
                        )
                        MyApp.mHandler.post {
                            conversationLiveData.value=listDataUiState
                        }
                        // callBack.onSuccess(createLiveData(infoList))
                    }

                    override fun onError(error: Int, errorMsg: String) {
                        //callBack.onError(error, errorMsg)
                        val listDataUiState=ListDataUiState(
                            isSuccess = false,
                            errorMsg = errorMsg ,
                            listData = arrayListOf<EaseConversationInfo>()
                        )
                        MyApp.mHandler.post {
                            conversationLiveData.value=listDataUiState
                        }
                    }
                })
            }
        )
    }

    /***
     * 邀请添加联系人
     */
    fun aysncAddContact(username:String, reason:String){
        requestHX(
            block = {
                EMClient.getInstance().contactManager().aysncAddContact(username, reason, object : EMCallBack {
                    override fun onSuccess() {
                        val listDataUiState=ListDataUiState(
                            isSuccess = true,
                            listData = arrayListOf("")
                        )
                        MyApp.mHandler.post {
                            addContactLiveData.value=listDataUiState
                        }

                    }

                    override fun onError(code: Int, error: String) {
                        val listDataUiState=ListDataUiState(
                            isSuccess = false,
                            errorMsg = error,
                            listData = arrayListOf("")
                        )
                        MyApp.mHandler.post {
                            addContactLiveData.value=listDataUiState
                        }
                    }
                    override fun onProgress(progress: Int, status: String) {

                    }
                })
            }
        )
    }



    fun loadSystemMsg(limit:Int=20){

        requestHX(
            block = {
                val emMessages:MutableList<EMMessage> = EMClient.getInstance().chatManager().searchMsgFromDB(
                    EMMessage.Type.TXT,
                    System.currentTimeMillis(),
                    limit,
                    EaseConstant.DEFAULT_SYSTEM_MESSAGE_ID,
                    EMConversation.EMSearchDirection.UP
                )
                sortData(emMessages)
                //将MutableList转成ArrayList
                var tempList= arrayListOf<EMMessage>()
                emMessages.forEach {
                    tempList.add(it)
                }
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    listData = tempList
                )
                MyApp.mHandler.post {
                    systemLiveData.value=listDataUiState
                }
                //inviteMsgObservable.setSource(MutableLiveData<T>(emMessages))


            }
        )

    }

    /***
     * 会话排序
     */
    fun sortData(messages: List<EMMessage>) {
        Collections.sort(messages) { o1, o2 ->
            val o1MsgTime = o1!!.msgTime
            val o2MsgTime = o2!!.msgTime
            (o2MsgTime - o1MsgTime).toInt()
        }
    }

    /***
     * 同意添加好友
     */
    fun  agreeInvite(msg:EMMessage){
        EaseThreadManager.getInstance().runOnIOThread {
            try {
                val statusParams: String =
                    msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_STATUS)
                val status: InviteMessageStatus = InviteMessageStatus.valueOf(statusParams)
                var message = ""
                if (status === InviteMessageStatus.BEINVITEED) { //accept be friends
                    message = MyApp.instance.getString(
                        R.string.demo_system_agree_invite,
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM)
                    )
                    EMClient.getInstance().contactManager()
                        .acceptInvitation(msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM))
                } else if (status === InviteMessageStatus.BEAPPLYED) { //accept application to join group
                    message = MyApp.instance.getString(
                        R.string.demo_system_agree_remote_user_apply_to_join_group,
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM)
                    )
                    EMClient.getInstance().groupManager().acceptApplication(
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM),
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_GROUP_ID)
                    )
                } else if (status === InviteMessageStatus.GROUPINVITATION) {
                    message = MyApp.instance.getString(
                        R.string.demo_system_agree_received_remote_user_invitation,
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_INVITER)
                    )
                    EMClient.getInstance().groupManager().acceptInvitation(
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_GROUP_ID),
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_INVITER)
                    )
                }
                msg.setAttribute(
                    DemoConstant.SYSTEM_MESSAGE_STATUS,
                    InviteMessageStatus.AGREED.name
                )
                msg.setAttribute(DemoConstant.SYSTEM_MESSAGE_REASON, message)
                val body = EMTextMessageBody(message)
                msg.body = body
                EaseSystemMsgManager.getInstance().updateMessage(msg)
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    listData = arrayListOf(message)
                )
                MyApp.mHandler.post {
                    agreeLiveData.value=listDataUiState
                    //LiveDataBus通知
                    LiveDataBus.get().with(DemoConstant.NOTIFY_CHANGE).postValue(EaseEvent.create(DemoConstant.NOTIFY_CHANGE, EaseEvent.TYPE.NOTIFY))
                }

            } catch (e: HyphenateException) {
                e.printStackTrace()
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg = e.message.toString(),
                    listData = arrayListOf("")
                )
                MyApp.mHandler.post {
                    agreeLiveData.value=listDataUiState
                }
            }
        }
    }

    /***
     * 拒绝添加好友
     */
    fun  refuseInvite(msg:EMMessage){
        EaseThreadManager.getInstance().runOnIOThread {
            try {
                val statusParams =
                    msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_STATUS)
                val status = InviteMessageStatus.valueOf(statusParams)
                var message = ""
                if (status === InviteMessageStatus.BEINVITEED) { //decline the invitation
                    message = MyApp.instance.getString(
                        R.string.demo_system_decline_invite,
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM)
                    )
                    EMClient.getInstance().contactManager()
                        .declineInvitation(msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM))
                } else if (status === InviteMessageStatus.BEAPPLYED) { //decline application to join group
                    message = MyApp.instance.getString(
                        R.string.demo_system_decline_remote_user_apply_to_join_group,
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM)
                    )
                    EMClient.getInstance().groupManager().declineApplication(
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM),
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_GROUP_ID),
                        ""
                    )
                } else if (status === InviteMessageStatus.GROUPINVITATION) {
                    message =MyApp.instance.getString(
                        R.string.demo_system_decline_received_remote_user_invitation,
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_INVITER)
                    )
                    EMClient.getInstance().groupManager().declineInvitation(
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_GROUP_ID),
                        msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_INVITER),
                        ""
                    )
                }
                msg.setAttribute(
                    DemoConstant.SYSTEM_MESSAGE_STATUS,
                    InviteMessageStatus.REFUSED.name
                )
                msg.setAttribute(DemoConstant.SYSTEM_MESSAGE_REASON, message)
                val body = EMTextMessageBody(message)
                msg.body = body
                EaseSystemMsgManager.getInstance().updateMessage(msg)
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    listData = arrayListOf(message)
                )
                MyApp.mHandler.post {
                    refuseLiveData.value=listDataUiState
                    //LiveDataBus通知
                    LiveDataBus.get().with(DemoConstant.NOTIFY_CHANGE).postValue(EaseEvent.create(DemoConstant.NOTIFY_CHANGE, EaseEvent.TYPE.NOTIFY))
                }
            } catch (e: HyphenateException) {
                e.printStackTrace()
                val listDataUiState=ListDataUiState(
                    isSuccess = false,
                    errorMsg = e.message.toString(),
                    listData = arrayListOf("")
                )
                MyApp.mHandler.post {
                    refuseLiveData.value=listDataUiState
                }
            }
        }
    }
    var contactLiveData: MutableLiveData<ListDataUiState<EaseUser>> = MutableLiveData()
    /**
     * 获取联系人列表
     * @param callBack
     */
    fun getContactList() {
        if (!isLoggedIn()) {
                ToastUtils.showShort("请先登录~")
            return
        }
        requestHX(
            block = {
                try {
                    var usernames: MutableList<String?> = EMClient.getInstance().contactManager().allContactsFromServer
                    val ids: List<String?> = EMClient.getInstance().contactManager().selfIdsOnOtherPlatform
                    if (usernames == null) {
                        usernames = java.util.ArrayList()
                    }
                    if (ids != null && ids.isNotEmpty()) {
                        usernames.addAll(ids)
                    }
                    val easeUsers: List<EaseUser> = EmUserEntity.parse(usernames)
                    if (usernames != null && usernames.isNotEmpty()) {
                        val blackListFromServer: List<String> =
                            EMClient.getInstance().contactManager().blackListFromServer
                        for (user in easeUsers) {
                            if (blackListFromServer != null && !blackListFromServer.isEmpty()) {
                                if (blackListFromServer.contains(user.username)) {
                                    user.contact = 1
                                }
                            }
                        }
                    }
                    sortUserData(easeUsers)
                    var tempList= arrayListOf<EaseUser>()
                    easeUsers.forEach {
                        tempList.add(it)
                    }
                    val listDataUiState=ListDataUiState(
                        isSuccess = true,
                        listData = tempList
                    )
                    MyApp.mHandler.post {
                        contactLiveData.value=listDataUiState
                    }
                } catch (e: HyphenateException) {
                    e.printStackTrace()
                    val listDataUiState=ListDataUiState(
                        isSuccess = false,
                        errorMsg = e.message.toString(),
                        listData = arrayListOf<EaseUser>()
                    )
                    MyApp.mHandler.post {
                        contactLiveData.value=listDataUiState
                    }
                }
            }
        )
    }

    private fun sortUserData(data: List<EaseUser>) {
        if (data == null || data.isEmpty()) {
            return
        }
        Collections.sort(data, object : Comparator<EaseUser> {
            override fun compare(lhs: EaseUser, rhs: EaseUser): Int {
                return if (lhs.initialLetter == rhs.initialLetter) {
                    lhs.nickname.compareTo(rhs.nickname)
                } else {
                    if ("#" == lhs.initialLetter) {
                        return 1
                    } else if ("#" == rhs.initialLetter) {
                        return -1
                    }
                    lhs.initialLetter.compareTo(rhs.initialLetter)
                }
            }
        })
    }


}
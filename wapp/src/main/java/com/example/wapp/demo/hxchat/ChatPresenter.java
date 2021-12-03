package com.example.wapp.demo.hxchat;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.wapp.R;
import com.example.wapp.demo.MyApp;
import com.example.wapp.demo.constant.DemoConstant;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.manager.EaseAtMessageHelper;
import com.hyphenate.easeui.manager.EaseChatPresenter;
import com.hyphenate.easeui.manager.EaseSystemMsgManager;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.util.EMLog;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 主要用于chat过程中的全局监听，并对相应的事件进行处理
 * {@link #init()}方法建议在登录成功以后进行调用
 */
public class ChatPresenter extends EaseChatPresenter {
    private static final String TAG = ChatPresenter.class.getSimpleName();
    private static final int HANDLER_SHOW_TOAST = 0;
    private static ChatPresenter instance;
    private LiveDataBus messageChangeLiveData;
    private boolean isGroupsSyncedWithServer = false;
    private boolean isContactsSyncedWithServer = false;
    private boolean isBlackListSyncedWithServer = false;
    private boolean isPushConfigsWithServer = false;
    private Context appContext;
    protected Handler handler;

    Queue<String> msgQueue = new ConcurrentLinkedQueue<>();

    private ChatPresenter() {
        appContext = MyApp.instance;
        initHandler(appContext.getMainLooper());
        messageChangeLiveData = LiveDataBus.get();
        //添加网络连接状态监听
       // DemoHelper.getInstance().getEMClient().addConnectionListener(new ChatConnectionListener());
        //添加多端登录监听
      //  DemoHelper.getInstance().getEMClient().addMultiDeviceListener(new ChatMultiDeviceListener());
        //添加群组监听
      //  DemoHelper.getInstance().getGroupManager().addGroupChangeListener(new ChatGroupListener());
        //添加联系人监听
       // DemoHelper.getInstance().getContactManager().setContactListener(new ChatContactListener());
        //添加聊天室监听
       // DemoHelper.getInstance().getChatroomManager().addChatRoomChangeListener(new ChatRoomListener());
        //添加对会话的监听（监听已读回执）
        EMClient.getInstance().chatManager().addConversationListener(new ChatConversationListener());
        EMClient.getInstance().contactManager().setContactListener(new ChatContactListener());
    }

    public static ChatPresenter getInstance() {
        if(instance == null) {
            synchronized (ChatPresenter.class) {
                if(instance == null) {
                    instance = new ChatPresenter();
                }
            }
        }
        return instance;
    }

    /**
     * 将需要登录成功进入MainActivity中初始化的逻辑，放到此处进行处理
     */
    public void init() {

    }

    public void initHandler(Looper looper) {
        handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                Object obj = msg.obj;
                switch (msg.what) {
                    case HANDLER_SHOW_TOAST :
                        if(obj instanceof String) {
                            String str = (String) obj;
                            //ToastUtils.showToast(str);
                            Toast.makeText(appContext, str, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        while (!msgQueue.isEmpty()) {
            showToast(msgQueue.remove());
        }
    }


    void showToast(final String message) {
        Log.d(TAG, "receive invitation to join the group：" + message);
        if (handler != null) {
            Message msg = Message.obtain(handler, HANDLER_SHOW_TOAST, message);
            handler.sendMessage(msg);
        } else {
            msgQueue.add(message);
        }
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        super.onMessageReceived(messages);
        EaseEvent event = EaseEvent.create(DemoConstant.MESSAGE_CHANGE_RECEIVE, EaseEvent.TYPE.MESSAGE);
        messageChangeLiveData.with(DemoConstant.MESSAGE_CHANGE_CHANGE).postValue(event);
        for (EMMessage message : messages) {
            EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
            EMLog.d(TAG, "onMessageReceived: " + message.getType());
            // 如果设置群组离线消息免打扰，则不进行消息通知
            List<String> disabledIds =EMClient.getInstance().pushManager().getNoPushGroups();
            if(disabledIds != null && disabledIds.contains(message.conversationId())) {
                return;
            }
            // in background, do not refresh UI, notify it in notification bar
            if(!MyApp.instance.getMLifecycleCallbacks().isFront()){
                getNotifier().notify(message);
            }
            //notify new message
            getNotifier().vibrateAndPlayTone(message);
        }
    }


    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
        EaseEvent event = EaseEvent.create(DemoConstant.MESSAGE_CHANGE_CMD_RECEIVE, EaseEvent.TYPE.MESSAGE);
        messageChangeLiveData.with(DemoConstant.MESSAGE_CHANGE_CHANGE).postValue(event);
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        super.onMessageRead(messages);
       // if(!(MyApp.instance.getMLifecycleCallbacks().current() instanceof ChatActivity)) {
            EaseEvent event = EaseEvent.create(DemoConstant.MESSAGE_CHANGE_RECALL, EaseEvent.TYPE.MESSAGE);
            messageChangeLiveData.with(DemoConstant.MESSAGE_CHANGE_CHANGE).postValue(event);
        //}
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        EaseEvent event = EaseEvent.create(DemoConstant.MESSAGE_CHANGE_RECALL, EaseEvent.TYPE.MESSAGE);
        messageChangeLiveData.with(DemoConstant.MESSAGE_CHANGE_CHANGE).postValue(event);
        for (EMMessage msg : messages) {
            if(msg.getChatType() == EMMessage.ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)){
                EaseAtMessageHelper.get().removeAtMeGroup(msg.getTo());
            }
            EMMessage msgNotification = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            EMTextMessageBody txtBody = new EMTextMessageBody(String.format(context.getString(R.string.msg_recall_by_user), msg.getFrom()));
            msgNotification.addBody(txtBody);
            msgNotification.setFrom(msg.getFrom());
            msgNotification.setTo(msg.getTo());
            msgNotification.setUnread(false);
            msgNotification.setMsgTime(msg.getMsgTime());
            msgNotification.setLocalTime(msg.getMsgTime());
            msgNotification.setChatType(msg.getChatType());
            msgNotification.setAttribute(DemoConstant.MESSAGE_TYPE_RECALL, true);
            msgNotification.setStatus(EMMessage.Status.SUCCESS);
            EMClient.getInstance().chatManager().saveMessage(msgNotification);
        }
    }

    private class ChatConversationListener implements EMConversationListener {

        @Override
        public void onCoversationUpdate() {

        }

        @Override
        public void onConversationRead(String from, String to) {
            EaseEvent event = EaseEvent.create(DemoConstant.CONVERSATION_READ, EaseEvent.TYPE.MESSAGE);
            messageChangeLiveData.with(DemoConstant.CONVERSATION_READ).postValue(event);
        }
    }



    private class   ChatContactListener implements EMContactListener {

        @Override
        public void onContactAdded(String username) {
            EMLog.i("ChatContactListener", "onContactAdded");
//            String[] userId = new String[1];
//            userId[0] = username;
//            EMClient.getInstance().userInfoManager().fetchUserInfoByUserId(userId, new EMValueCallBack<Map<String, EMUserInfo>>() {
//                @Override
//                public void onSuccess(Map<String, EMUserInfo> value) {
//                    EMUserInfo userInfo = value.get(username);
//                    EmUserEntity entity = new EmUserEntity();
//                    entity.setUsername(username);
//                    if(userInfo != null){
//                        entity.setNickname(userInfo.getNickName());
//                        entity.setEmail(userInfo.getEmail());
//                        entity.setAvatar(userInfo.getAvatarUrl());
//                        entity.setBirth(userInfo.getBirth());
//                        entity.setGender(userInfo.getGender());
//                        entity.setExt(userInfo.getExt());
//                        entity.setContact(0);
//                        entity.setSign(userInfo.getSignature());
//                    }
//                    DemoHelper.getInstance().getModel().insert(entity);
//                    DemoHelper.getInstance().updateContactList();
//                    EaseEvent event = EaseEvent.create(DemoConstant.CONTACT_ADD, EaseEvent.TYPE.CONTACT);
//                    event.message = username;
//                    messageChangeLiveData.with(DemoConstant.CONTACT_ADD).postValue(event);
//
//                    showToast(context.getString(R.string.demo_contact_listener_onContactAdded, username));
//                    EMLog.i(TAG, context.getString(R.string.demo_contact_listener_onContactAdded, username));
//                }
//
//                @Override
//                public void onError(int error, String errorMsg) {
//                    EMLog.i(TAG, context.getString(R.string.demo_contact_get_userInfo_failed) +  username + "error:" + error + " errorMsg:" +errorMsg);
//                }
//            });
        }

        @Override
        public void onContactDeleted(String username) {
            EMLog.i("ChatContactListener", "onContactDeleted");
//            boolean deleteUsername = DemoHelper.getInstance().getModel().isDeleteUsername(username);
//            int num = DemoHelper.getInstance().deleteContact(username);
//            DemoHelper.getInstance().updateContactList();
//            EaseEvent event = EaseEvent.create(DemoConstant.CONTACT_DELETE, EaseEvent.TYPE.CONTACT);
//            event.message = username;
//            messageChangeLiveData.with(DemoConstant.CONTACT_DELETE).postValue(event);
//
//            if(deleteUsername || num == 0) {
//                showToast(context.getString(R.string.demo_contact_listener_onContactDeleted, username));
//                EMLog.i(TAG, context.getString(R.string.demo_contact_listener_onContactDeleted, username));
//            }else {
//                //showToast(context.getString(R.string.demo_contact_listener_onContactDeleted_by_other, username));
//                EMLog.i(TAG, context.getString(R.string.demo_contact_listener_onContactDeleted_by_other, username));
//            }
        }



        @Override
        public void onContactInvited(String username, String reason) {
            EMLog.i("ChatContactListener", "onContactInvited");
            List<EMMessage> allMessages = EaseSystemMsgManager.getInstance().getAllMessages();
            if(allMessages != null && !allMessages.isEmpty()) {
                for (EMMessage message : allMessages) {
                    Map<String, Object> ext = message.ext();
                    if(ext != null && !ext.containsKey(DemoConstant.SYSTEM_MESSAGE_GROUP_ID)
                            && (ext.containsKey(DemoConstant.SYSTEM_MESSAGE_FROM) && TextUtils.equals(username, (String)ext.get(DemoConstant.SYSTEM_MESSAGE_FROM)))) {
                        EaseSystemMsgManager.getInstance().removeMessage(message);
                    }
                }
            }

            Map<String, Object> ext = EaseSystemMsgManager.getInstance().createMsgExt();
            ext.put(DemoConstant.SYSTEM_MESSAGE_FROM, username);
            ext.put(DemoConstant.SYSTEM_MESSAGE_REASON, reason);
            ext.put(DemoConstant.SYSTEM_MESSAGE_STATUS, InviteMessageStatus.BEINVITEED.name());
            EMMessage message = EaseSystemMsgManager.getInstance().createMessage(PushAndMessageHelper.getSystemMessage(ext), ext);

            notifyNewInviteMessage(message);
            EaseEvent event = EaseEvent.create(DemoConstant.CONTACT_CHANGE, EaseEvent.TYPE.CONTACT);
            messageChangeLiveData.with(DemoConstant.CONTACT_CHANGE).postValue(event);

            showToast(context.getString(InviteMessageStatus.BEINVITEED.getMsgContent(), username));
            EMLog.i(TAG, context.getString(InviteMessageStatus.BEINVITEED.getMsgContent(), username));
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            EMLog.i("ChatContactListener", "onFriendRequestAccepted");
//            List<EMMessage> allMessages = EaseSystemMsgManager.getInstance().getAllMessages();
//            if(allMessages != null && !allMessages.isEmpty()) {
//                for (EMMessage message : allMessages) {
//                    Map<String, Object> ext = message.ext();
//                    if(ext != null && (ext.containsKey(DemoConstant.SYSTEM_MESSAGE_FROM)
//                            && TextUtils.equals(username, (String)ext.get(DemoConstant.SYSTEM_MESSAGE_FROM)))) {
//                        updateMessage(message);
//                        return;
//                    }
//                }
//            }
//            Map<String, Object> ext = EaseSystemMsgManager.getInstance().createMsgExt();
//            ext.put(DemoConstant.SYSTEM_MESSAGE_FROM, username);
//            ext.put(DemoConstant.SYSTEM_MESSAGE_STATUS, InviteMessageStatus.BEAGREED.name());
//            EMMessage message = EaseSystemMsgManager.getInstance().createMessage(PushAndMessageHelper.getSystemMessage(ext), ext);
//
//            notifyNewInviteMessage(message);
//            EaseEvent event = EaseEvent.create(DemoConstant.CONTACT_CHANGE, EaseEvent.TYPE.CONTACT);
//            messageChangeLiveData.with(DemoConstant.CONTACT_CHANGE).postValue(event);
//
//            showToast(context.getString(InviteMessageStatus.BEAGREED.getMsgContent()));
//            EMLog.i(TAG, context.getString(InviteMessageStatus.BEAGREED.getMsgContent()));
        }

        @Override
        public void onFriendRequestDeclined(String username) {
            EMLog.i("ChatContactListener", "onFriendRequestDeclined");
//            Map<String, Object> ext = EaseSystemMsgManager.getInstance().createMsgExt();
//            ext.put(DemoConstant.SYSTEM_MESSAGE_FROM, username);
//            ext.put(DemoConstant.SYSTEM_MESSAGE_STATUS, InviteMessageStatus.BEREFUSED.name());
//            EMMessage message = EaseSystemMsgManager.getInstance().createMessage(PushAndMessageHelper.getSystemMessage(ext), ext);
//
//            notifyNewInviteMessage(message);
//
//            EaseEvent event = EaseEvent.create(DemoConstant.CONTACT_CHANGE, EaseEvent.TYPE.CONTACT);
//            messageChangeLiveData.with(DemoConstant.CONTACT_CHANGE).postValue(event);
//            showToast(context.getString(InviteMessageStatus.BEREFUSED.getMsgContent(), username));
//            EMLog.i(TAG, context.getString(InviteMessageStatus.BEREFUSED.getMsgContent(), username));
        }
    }


    private void notifyNewInviteMessage(EMMessage msg) {
        // notify there is new message
        getNotifier().vibrateAndPlayTone(null);
    }







}

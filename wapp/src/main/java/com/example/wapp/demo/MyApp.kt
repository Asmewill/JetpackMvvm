package com.example.wapp.demo

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.blankj.utilcode.util.Utils
import com.example.wapp.demo.hxchat.UserActivityLifecycleCallbacks
import com.example.wapp.demo.loadcallback.ErrorCallback
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.delegate.*
import com.hyphenate.easeui.manager.EaseMessageTypeSetManager
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.*

/**
 * Created by jsxiaoshui on 2021/8/17
 */
class MyApp : Application() {
    val mLifecycleCallbacks: UserActivityLifecycleCallbacks = UserActivityLifecycleCallbacks()
    var isSDKInit = false //SDK是否初始化

    companion object{
        lateinit var instance :MyApp
        lateinit var  mHandler:Handler
    }
    override fun onCreate() {
        super.onCreate()
        mHandler= Handler(Looper.getMainLooper())
        instance=this
        MMKV.initialize(filesDir.absolutePath+"/mmkv")
        initLoadSir()
        initHx()
        Utils.init(this)  //blankj.utilcode
    }

    private fun initLoadSir() {
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(LoadingCallback2())
            .addCallback(ErrorCallback())//错误
            .addCallback(ErrorCallback2())//错误
            .addCallback(EmptyCallback())//空
            .addCallback(EmptyCallback2())
            //.setDefaultCallback(LoadingCallback::class.java)//设置默认加载状态页
            .commit()
    }

    private fun initHx(){
       if(initSDK(this)){
           // debug mode, you'd better set it to false, if you want release your App officially.
           EMClient.getInstance().setDebugMode(true)
           // set Call options
          // setCallOptions(context)
           //初始化推送
         //  initPush(context)
           //注册call Receiver
           //initReceiver(context);
           //初始化ease ui相关
       //    initEaseUI(context)
           //注册对话类型
           registerConversationType()
           //callKit初始化
      //     InitCallKit(context)
           //启动获取用户信息线程
//           fetchUserInfoList = FetchUserInfoList.getInstance()
//           fetchUserRunnable = FetchUserRunnable()
//           fetchUserTread = Thread(fetchUserRunnable)
//           fetchUserTread.start()

       }
    }

    /**
     * 注册对话类型
     */
    private fun registerConversationType() {
        EaseMessageTypeSetManager.getInstance()
            .addMessageType(EaseExpressionAdapterDelegate::class.java) //自定义表情
            .addMessageType(EaseFileAdapterDelegate::class.java) //文件
            .addMessageType(EaseImageAdapterDelegate::class.java) //图片
            .addMessageType(EaseLocationAdapterDelegate::class.java) //定位
            .addMessageType(EaseVideoAdapterDelegate::class.java) //视频
            .addMessageType(EaseVoiceAdapterDelegate::class.java) //声音
            //.addMessageType(ChatConferenceInviteAdapterDelegate::class.java) //语音邀请
           // .addMessageType(ChatRecallAdapterDelegate::class.java) //消息撤回
           // .addMessageType(ChatVideoCallAdapterDelegate::class.java) //视频通话
          //  .addMessageType(ChatVoiceCallAdapterDelegate::class.java) //语音通话
          //  .addMessageType(ChatUserCardAdapterDelegate::class.java) //名片消息
            .addMessageType(EaseCustomAdapterDelegate::class.java) //自定义消息
           // .addMessageType(ChatNotificationAdapterDelegate::class.java) //入群等通知消息
            .setDefaultMessageType(EaseTextAdapterDelegate::class.java) //文本
    }

    /**
     * 初始化SDK
     * @param context
     * @return
     */
    private fun initSDK(context: Context): Boolean {
        // 根据项目需求对SDK进行配置
        val options: EMOptions = initChatOptions(context)

//        options.setIMSer   //配置自定义的rest server和im server
////        options.setRestServer("a1-hsb.easemob.com");ver("106.75.100.247");
//        options.setImPort(6717);

//        options.setRestServer("a41.easemob.com");
//        options.setIMServer("msync-im-41-tls-test.easemob.com");
//        options.setImPort(6717);
        // 初始化SDK
        isSDKInit = EaseIM.getInstance().init(context, options)
        //设置删除用户属性数据超时时间
      //  demoModel.setUserInfoTimeOut(30 * 60 * 1000)
        //更新过期用户属性列表
     //   updateTimeoutUsers()
       // mianContext = context
        return isSDKInit
    }

    /**
     * 根据自己的需要进行配置
     * @param context
     * @return
     */
    private fun initChatOptions(context: Context): EMOptions {
       // Log.d(com.hyphenate.easeim.DemoHelper.TAG, "init HuanXin Options")
        val options = EMOptions()
        // 设置是否自动接受加好友邀请,默认是true
        options.acceptInvitationAlways = false
        // 设置是否需要接受方已读确认
        options.requireAck = true
        // 设置是否需要接受方送达确认,默认false
        options.requireDeliveryAck = false
        /**
         * NOTE:你需要设置自己申请的账号来使用三方推送功能，详见集成文档
         */
//        val builder = EMPushConfig.Builder(context)
//        builder.enableVivoPush() // 需要在AndroidManifest.xml中配置appId和appKey
//            .enableMeiZuPush("134952", "f00e7e8499a549e09731a60a4da399e3")
//            .enableMiPush("2882303761517426801", "5381742660801")
//            .enableOppoPush(
//                "0bb597c5e9234f3ab9f821adbeceecdb",
//                "cd93056d03e1418eaa6c3faf10fd7537"
//            )
//            .enableHWPush() // 需要在AndroidManifest.xml中配置appId
//            .enableFCM("921300338324")
//        options.pushConfig = builder.build()

//        //set custom servers, commonly used in private deployment
//        if (demoModel.isCustomSetEnable()) {
//            if (demoModel.isCustomServerEnable() && demoModel.getRestServer() != null && demoModel.getIMServer() != null) {
//                // 设置rest server地址
//                options.restServer = demoModel.getRestServer()
//                // 设置im server地址
//                options.setIMServer(demoModel.getIMServer())
//                //如果im server地址中包含端口号
//                if (demoModel.getIMServer().contains(":")) {
//                    options.setIMServer(demoModel.getIMServer().split(":").get(0))
//                    // 设置im server 端口号，默认443
//                    options.imPort = Integer.valueOf(demoModel.getIMServer().split(":").get(1))
//                } else {
//                    //如果不包含端口号
//                    if (demoModel.getIMServerPort() !== 0) {
//                        options.imPort = demoModel.getIMServerPort()
//                    }
//                }
//            }
//        }
//        if (demoModel.isCustomAppkeyEnabled() && !TextUtils.isEmpty(demoModel.getCutomAppkey())) {
//            // 设置appkey
//            options.appKey = demoModel.getCutomAppkey()
//        }
//        val imServer = options.imServer
//        val restServer = options.restServer

        // 设置是否允许聊天室owner离开并删除会话记录，意味着owner再不会受到任何消息
        options.allowChatroomOwnerLeave(true)
        // 设置退出(主动和被动退出)群组时是否删除聊天消息
        options.isDeleteMessagesAsExitGroup = false
        // 设置是否自动接受加群邀请
        options.isAutoAcceptGroupInvitation = true
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载
        options.autoTransferMessageAttachments = true
        // 是否自动下载缩略图，默认是true为自动下载
        options.setAutoDownloadThumbnail(true)
        return options
    }

}

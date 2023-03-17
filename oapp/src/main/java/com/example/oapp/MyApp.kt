package com.example.oapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig

import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.ui.ErrorActivity
import com.example.oapp.ui.SplashActivity
import com.example.oapp.utils.SettingUtil
import com.kingja.loadsir.core.LoadSir
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.*
import org.litepal.LitePal
import kotlin.properties.Delegates

/**
 * Created by jsxiaoshui on 2021/6/24
 */
class MyApp : MultiDexApplication() {
    companion object {
        var context: Context by Delegates.notNull()
        lateinit var instance: Application
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        LitePal.initialize(this)//数据库初始化
        initLoadSir()
        setTheme()
        setCaoConfig()
        initUmeng()
        initARouter()
        initBugly();
    }

    private fun initBugly() {
       CrashReport.initCrashReport(getApplicationContext(), "23554e2ebb", true)//建议在测试阶段建议设置成true，发布时设置为false。
    }

    //初始化路由框架
    private fun initARouter() {
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }


    private fun setCaoConfig() {
        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
            .restartActivity(SplashActivity::class.java) // 重启的activity
            .errorActivity(ErrorActivity::class.java) //发生错误跳转的activity
            .apply()
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

    /***
     * 设置主题
     */
    fun setTheme() {
        if (SettingUtil.getIsNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SettingUtil.setIsNightMode(true)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            SettingUtil.setIsNightMode(false)
        }
    }

    private fun initUmeng() {
        //2.在Application.oncreate()中调用预初始化函数
        UMConfigure.preInit(this, "610b591226e9627944b70421","umeng")
        UMConfigure.setLogEnabled(true)
        //3.客户端用户同意隐私政策后，正式初始化友盟+SDK
        UMConfigure.init(
            this,
            "610b591226e9627944b70421",
            "umeng",
            UMConfigure.DEVICE_TYPE_PHONE, ""
        )
        // 页面数据采集模式
        // setPageCollectionMode接口参数说明：
        // 1. MobclickAgent.PageMode.AUTO: 建议大多数用户使用本采集模式，SDK在此模式下自动采集Activity
        // 页面访问路径，开发者不需要针对每一个Activity在onResume/onPause函数中进行手动埋点。在此模式下，
        // 开发者如需针对Fragment、CustomView等自定义页面进行页面统计，直接调用MobclickAgent.onPageStart/
        // MobclickAgent.onPageEnd手动埋点即可。此采集模式简化埋点工作，唯一缺点是在Android 4.0以下设备中
        // 统计不到Activity页面数据和各类基础指标(提示：目前Android 4.0以下设备市场占比已经极小)。
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

        //Oracle 安装之后怎么使用  sqlplus




    }
}
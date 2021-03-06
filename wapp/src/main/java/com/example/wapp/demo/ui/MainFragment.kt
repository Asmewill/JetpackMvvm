package com.example.wapp.demo.ui

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentMainBinding
import com.example.wapp.demo.constant.DemoConstant
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initMain
import com.example.wapp.demo.hxchat.ChatPresenter
import com.example.wapp.demo.hxchat.LiveDataBus
import com.example.wapp.demo.permission.PermissionsManager
import com.example.wapp.demo.permission.PermissionsResultAction
import com.example.wapp.demo.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.hyphenate.chat.EMClient
import com.hyphenate.easeui.model.EaseEvent
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.logging.Handler

/**
 * Created by jsxiaoshui on 2021/8/18
 */
class MainFragment :BaseVmDbFragment<MainViewModel,FragmentMainBinding>() {
    private  var tv_msg:TextView?=null
    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun createViewModel(): MainViewModel {
        return ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun initView() {
       mainViewpager.initMain(this)
       mainBottom.init {
            when(it){
                R.id.menu_main->{
                    mainViewpager.setCurrentItem(0,false)
                }
                R.id.menu_project-> {
                    mainViewpager.setCurrentItem(1,false)
                }
                R.id.menu_system->{
                    mainViewpager.setCurrentItem(2,false)
                }
                R.id.menu_public->{
                    mainViewpager.setCurrentItem(3,false)
                }
                R.id.menu_me-> {
                    mainViewpager.setCurrentItem(4,false)
                }
            }
       }
        /****
         * ?????????????????????????????????????????????????????????
         */
       LiveDataBus.get().with(DemoConstant.GROUP_CHANGE, EaseEvent::class.java)
            .observe(this, Observer {
                checkUnreadMsg()
            })
        LiveDataBus.get().with(DemoConstant.NOTIFY_CHANGE, EaseEvent::class.java)
            .observe(this, Observer {
                checkUnreadMsg()
            })
        LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent::class.java)
            .observe(this, Observer {
                checkUnreadMsg()
            })
        LiveDataBus.get().with(DemoConstant.CONVERSATION_DELETE, EaseEvent::class.java)
            .observe(this, Observer {
                checkUnreadMsg()
            })
        LiveDataBus.get().with(DemoConstant.CONVERSATION_READ, EaseEvent::class.java)
            .observe(this, Observer {
                checkUnreadMsg()
            })
        LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE, EaseEvent::class.java)
            .observe(this, Observer {
                checkUnreadMsg()
            })
        //??????????????????
        addTabBadge()
    }


    /**
     * ??????BottomNavigationView?????????item??????????????????
     */

    private fun addTabBadge() {
        val menuView = mainBottom.getChildAt(0) as BottomNavigationMenuView
        val childCount = menuView.childCount
        Log.e("TAG", "bottom child count = $childCount")
        var itemTab: BottomNavigationItemView = menuView.getChildAt(3) as BottomNavigationItemView
        val badge: View = LayoutInflater.from(mActivity).inflate(R.layout.demo_badge_home, menuView, false)
        tv_msg= badge?.findViewById<TextView>(R.id.tv_main_home_msg)
        itemTab.addView(badge)

    }
    override fun initData() {
        requestPermissions()
        //??????????????????????????????????????????????????????????????????????????????...
        ChatPresenter.getInstance().init()
        checkUnreadMsg()

    }

    override fun createObserver() {

    }
    fun  checkUnreadMsg(){
        android.os.Handler().postDelayed({
            val unreadMessageCount: Int = EMClient.getInstance().chatManager().unreadMessageCount
            val count: String = getUnreadCount(unreadMessageCount)
            if (!TextUtils.isEmpty(count)) {
                tv_msg?.visibility = View.VISIBLE
                tv_msg?.text = count
            } else {
                tv_msg?.visibility = View.GONE
            }
        },500)



    }

    /**
     * ????????????????????????
     * @param count
     * @return
     */
    private fun getUnreadCount(count: Int): String {
        if (count <= 0) {
            return ""
        }
        return if (count > 99) {
            "99+"
        } else count.toString()
    }

    // TODO: 2019/12/19 0019 ?????????????????????
    private fun requestPermissions() {
        PermissionsManager.getInstance()
            .requestAllManifestPermissionsIfNecessary(mActivity, object : PermissionsResultAction() {
                override fun onGranted() {


                }
                override fun onDenied(permission: String?) {

                }
            })
    }
}
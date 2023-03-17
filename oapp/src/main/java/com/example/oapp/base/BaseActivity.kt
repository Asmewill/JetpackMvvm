package com.example.oapp.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.oapp.R
import com.example.oapp.constant.Constant
import com.example.oapp.event.BaseEvent
import com.example.oapp.utils.KeyBoardUtil
import com.example.oapp.utils.Preference
import com.example.oapp.utils.SettingUtil
import com.example.oapp.utils.StatusBarUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by jsxiaoshui on 2021/6/24
 */
abstract  class BaseActivity :AppCompatActivity(){
    protected var mThemeColor=SettingUtil.getColor()
    protected var isLogin  by Preference(Constant.IS_LOGIN,false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        //clearFragmentsBeforeCreate()
        EventBus.getDefault().register(this)
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        initColor()
    }

     abstract fun attachLayoutRes(): Int
     abstract  fun initView()
     abstract  fun initData()


     @Subscribe(threadMode = ThreadMode.MAIN)
      fun baseEvent(event:BaseEvent){

     }

    /***
     * 只有Open的方法才可以被重写
     */
    open fun initColor() {
        mThemeColor=if(!SettingUtil.getIsNightMode()){
            SettingUtil.getColor()
        }else{
            resources.getColor(R.color.colorPrimary)
        }
        StatusBarUtil.setColor(this,mThemeColor,0)
        if(this.supportActionBar!=null){
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            if(SettingUtil.getNavBar()){
                window.navigationBarColor=mThemeColor
            }else{
                window.navigationBarColor= Color.BLACK
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /***
     * 点击空白区域让键盘可以自动落下
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action== MotionEvent.ACTION_UP){
            var foucusView=currentFocus
            if(KeyBoardUtil.isHideKeyboard(foucusView,ev)){
                KeyBoardUtil.hideKeyBoard(this,foucusView)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    /**
     * 处理因为Activity重建导致的fragment叠加问题
     */
    open fun clearFragmentsBeforeCreate() {
        val fragments = supportFragmentManager.fragments
        if (fragments.size == 0) {
            return
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        for (fragment in fragments) {
            fragmentTransaction.remove(fragment!!)
        }
        fragmentTransaction.commitNow()
    }

}
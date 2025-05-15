package com.example.oapp.base

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.oapp.R
import com.example.oapp.utils.DialogUtil
import com.example.oapp.utils.KeyBoardUtil
import com.example.oapp.utils.SettingUtil
import com.example.oapp.utils.StatusBarUtil

/**
 * Created by jsxiaoshui on 2021/7/22
 */
abstract class BaseVmActivity<VM : BaseViewModel>:AppCompatActivity() {
    protected var mThemeColor=SettingUtil.getColor()
    lateinit var mViewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!isUseDB()){
            setContentView(layoutId())
        }else{
            initDataBind()
        }
        mViewModel=createViewModel()
        createObserver()
        registerUiChange()
        initView()
        initData()

    }
    override fun onResume() {
        super.onResume()
        initColor()
    }


    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.showLoadingLiveData.observeInActivity(this, Observer {
            showLoading(it)
        })
        //关闭弹窗
        mViewModel.dismissDialogLiveData.observeInActivity(this, Observer {
            dismissLoading()
        })
    }

    private fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    //设置是否使用DataBinding
   open fun isUseDB():Boolean{
        return  false
    }


    abstract  fun createViewModel():VM
    abstract fun layoutId(): Int
    abstract  fun initView()
    abstract  fun initData()
    abstract  fun createObserver()
    //如果使用DataBinding,供子类重写
    open fun initDataBind() {
    }


    private var loadingDialog: ProgressDialog? = null

    /**
     * 打开等待框
     */
    fun showLoading(message: String = "loading") {
        if (!this.isFinishing) {
            loadingDialog= DialogUtil.getWaitDialog(this,message)
            loadingDialog?.show()
        }
    }
    /***
     * 只有Open的方法才可以被重写
     */
    open fun initColor() {
        //获取主题色
        mThemeColor=if(!SettingUtil.getIsNightMode()){
            SettingUtil.getColor()
        }else{
            resources.getColor(R.color.colorPrimary)
        }
        //设置状态栏主题色值
        StatusBarUtil.setColor(this,mThemeColor,0)
        //设置ActionBar颜色为主题色
        if(this.supportActionBar!=null){
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
        //虚拟导航栏的颜色
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            if(SettingUtil.getNavBar()){
                window.navigationBarColor=mThemeColor
            }else{
                window.navigationBarColor = Color.BLACK
            }
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
}
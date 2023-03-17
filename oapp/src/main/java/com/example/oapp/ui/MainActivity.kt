package com.example.oapp.ui

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.base.BaseActivity
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.UserInfoBody
import com.example.oapp.constant.Constant
import com.example.oapp.event.LoginEvent
import com.example.oapp.event.ThemeEvent
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.ui.fragment.*
import com.example.oapp.utils.DialogUtil
import com.example.oapp.utils.Preference
import com.example.oapp.utils.SettingUtil
import com.example.oapp.utils.SharedPreUtil
import com.example.oapp.viewmodel.EventViewModel
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.Subscribe

/**
 * Created by jsxiaoshui on 2021/6/25
 */
@Route(path = Constant.PagePath.MAIN)
class MainActivity :BaseActivity() {
    //Fragment导航索引
    private val FRAGMENT_HOME=0
    private val FRAGMENT_KNOWLEDGE=1
    private val FRAGMENT_WECHAT=2
    private val FRAGMENT_NAVIGATION=3
    private val FRAGMENT_PROJECT=4
    private var nav_view_header:LinearLayout?=null
    private var ll_header:LinearLayout?=null
    private var tv_username:TextView?=null
    private var tv_user_grade:TextView?=null
    private var tv_user_rank:TextView?=null
    private var iv_rank:ImageView?=null
    private var userName:String by Preference(Constant.USER_NAME_KEY,"")
    override fun attachLayoutRes(): Int {
        return R.layout.activity_main
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search->{
                ARouter.getInstance()
                    .build(Constant.PagePath.SEARCH)
                    .navigation(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initView() {
//         getSupportFragmentManager().beginTransaction()
//            .add(R.id.container, FlutterActivity.createFragment("/"))
//            .addToBackStack("flutter")
//            .commit();

        createObserver()
        //let的基础用法
        toolbar?.let{
            setTitle("首页")
            setSupportActionBar(it)
            return@let 88//函数最后一行或者return 表达式表示函数的返回值
        }
        //run的基础用法
        bottom_navigation.run {
            this.labelVisibilityMode=LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            this.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.action_home->{
                        showFragment(FRAGMENT_HOME)
                    }
                    R.id.action_knowledge_system->{
                         showFragment(FRAGMENT_KNOWLEDGE)
                    }
                    R.id.action_wechat->{
                        showFragment(FRAGMENT_WECHAT)
                    }
                    R.id.action_navigation->{
                        showFragment(FRAGMENT_NAVIGATION)
                    }
                    R.id.action_project->{
                        showFragment(FRAGMENT_PROJECT)
                    }
                }
                 true  //lambda最后一行就是返回值
            }
             888 //函数最后一行或者return 表达式表示函数的返回值
        }
        //with的基础用法[原始写法]        给DrawerLayout添加Menu ICON和监听事件
        with(drawer_layout,{
            var toggle=ActionBarDrawerToggle(this@MainActivity,this,toolbar,0,0)
            this.addDrawerListener(toggle)
            toggle.syncState()
            return@with 8888
        })

        nav_view?.let {
            it.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.nav_score->{
                        if(isLogin){
                            ARouter.getInstance()
                                .build(Constant.PagePath.SCORE)
                                .navigation(this)
                        }else{
                            ARouter.getInstance()
                                .build(Constant.PagePath.LOGIN)
                                .navigation(this)
                        }
                    }
                    R.id.nav_collect->{
                        if(isLogin){
                            ARouter.getInstance()
                                .build(Constant.PagePath.COMMON)
                                .withString(Constant.PAGE_TYPE,Constant.Type.COLLECT_TYPE_KEY)
                                .navigation(this)
                        }else{
                           ARouter.getInstance()
                                .build(Constant.PagePath.LOGIN)
                                .navigation(this)
                        }

                    }
                    R.id.nav_todo->{
                        if(isLogin){
                            ARouter.getInstance()
                                .build(Constant.PagePath.TODO)
                                .navigation(this)
                        }else{
                            ARouter.getInstance()
                                .build(Constant.PagePath.LOGIN)
                                .navigation(this)
                        }
                    }
                    R.id.nav_night_mode->{
                        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)//放在这里不会0.5s黑屏
                        if(SettingUtil.getIsNightMode()){
                            SettingUtil.setIsNightMode(false)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }else{
                            SettingUtil.setIsNightMode(true)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                      //  recreate()
                    }
                    R.id.nav_setting->{
                        ARouter.getInstance()
                            .build(Constant.PagePath.SETTING)
                            .navigation(this)
                    }
                    R.id.nav_about_us->{
                        ARouter.getInstance()
                            .build(Constant.PagePath.COMMON)
                            .withString(Constant.PAGE_TYPE,Constant.Type.ABOUT_US_TYPE_KEY)
                            .navigation(this)
                    }
                    R.id.nav_logout->{
                        DialogUtil.getConfimDialog(this@MainActivity,"确定退出登录吗?", DialogInterface.OnClickListener {
                                dialog, which ->
                            loginOut()

                        }).show()
                    }
                    else->{

                    }
                }
                return@setNavigationItemSelectedListener true
            }
            nav_view_header=it.getHeaderView(0).findViewById(R.id.nav_view_header)
            ll_header=it.getHeaderView(0).findViewById(R.id.ll_header)
            tv_username=it.getHeaderView(0).findViewById(R.id.tv_username)
            tv_user_grade=it.getHeaderView(0).findViewById(R.id.tv_user_grade)
            tv_user_rank=it.getHeaderView(0).findViewById(R.id.tv_user_rank)
            iv_rank=it.getHeaderView(0).findViewById(R.id.iv_rank)
            iv_rank?.setOnClickListener {
                if(isLogin){
                    ARouter.getInstance().build(Constant.PagePath.RANKLIST).navigation()
                }else{
                    ARouter.getInstance().build(Constant.PagePath.LOGIN).navigation()
                }
            }
            it.menu.findItem(R.id.nav_logout).isVisible = isLogin
            if(isLogin){
                tv_username?.text=userName
                getUserInfo()
            }
            nav_view_header?.setOnClickListener {
                if(!isLogin){
                    ARouter.getInstance().build(Constant.PagePath.LOGIN).navigation()
                }
            }

        }
        showFragment(FRAGMENT_HOME)


    }

    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (homeFragment != null) {
                fragmentTransaction.remove(homeFragment!!)
            }
            if (knowledgeFragment != null) {
                fragmentTransaction.remove(knowledgeFragment!!)
            }
            if (navigationFragment != null) {
                fragmentTransaction.remove(navigationFragment!!)
            }
            if (projectFragment != null) {
                fragmentTransaction.remove(projectFragment!!)
            }
            if (weChatFragment != null) {
                fragmentTransaction.remove(weChatFragment!!)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }
    private fun createObserver() {
        //修改主题颜色
        EventViewModel.themeColorLiveData.observeInActivity(this) {
            initColor()
            nav_view.getHeaderView(0).setBackgroundColor(mThemeColor)
            floating_action_btn.backgroundTintList= ColorStateList.valueOf(mThemeColor)
            //非黑夜模式才设置
            if(!SettingUtil.getIsNightMode()){
              bottom_navigation?.itemTextColor=SettingUtil.getColorStateList(mThemeColor)
              bottom_navigation?.itemIconTintList=SettingUtil.getColorStateList(mThemeColor)
            }
        }
    }

    private fun loginOut() {
        HttpRetrofit.apiService.logout().applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<Any>>{
            override fun onSuccess(t: HttpResult<Any>) {
                  isLogin=false
                  userName=""
                  tv_user_grade?.text="--"
                  tv_user_rank?.text="--"
                  tv_username?.text="去登陆"
                  nav_view?.menu?.findItem(R.id.nav_logout)?.isVisible=false
                if(drawer_layout.isDrawerOpen(nav_view)){
                    drawer_layout.closeDrawers()
                }
                //清除SharePreference
                SharedPreUtil.removeString(this@MainActivity,Constant.USER_INFO)
                SharedPreUtil.removeString(this@MainActivity,Constant.LOGIN_BEAN)
            }

            override fun onFailture(t: Throwable) {
                showToast("退出失败，请重试...")

            }

        }))
    }
    private  var  homeFragment:HomeFragment?=null
    private  var  knowledgeFragment: KnowledgeFragment?=null
    private  var weChatFragment: WeChatFragment?=null
    private  var  navigationFragment: NavigationFragment?=null
    private  var projectFragment: ProjectFragment?=null

    private fun showFragment(mIndex:Int){
        val transaction=supportFragmentManager.beginTransaction();
        hideAllFragment(transaction)
        when(mIndex){
            FRAGMENT_HOME->{
                toolbar.title="首页"
                if(homeFragment==null){
                    homeFragment=HomeFragment()
                    transaction.add(R.id.container,homeFragment!!)
                }else{
                    transaction.show(homeFragment!!)
                }
            }
            FRAGMENT_KNOWLEDGE->{
                toolbar.title="知识体系"
                if(knowledgeFragment==null){
                    knowledgeFragment= KnowledgeFragment()
                    transaction.add(R.id.container,knowledgeFragment!!)
                }else{
                    transaction.show(knowledgeFragment!!)
                }

            }
            FRAGMENT_WECHAT->{
                toolbar.title="公众号"
                if(weChatFragment==null){
                    weChatFragment= WeChatFragment()
                    transaction.add(R.id.container,weChatFragment!!)
                }else{
                    transaction.show(weChatFragment!!)
                }

            }
            FRAGMENT_NAVIGATION->{
                toolbar.title="导航"
                if(navigationFragment==null){
                    navigationFragment= NavigationFragment()
                    transaction.add(R.id.container,navigationFragment!!)
                }else{
                    transaction.show(navigationFragment!!)
                }
            }
            FRAGMENT_PROJECT->{
                toolbar.title="项目"
                if(projectFragment==null){
                    projectFragment= ProjectFragment()
                    transaction.add(R.id.container,projectFragment!!)
                }else{
                    transaction.show(projectFragment!!)
                }
            }
            else->{

            }
        }
        transaction.commit()
    }
    private fun hideAllFragment(trasaction: FragmentTransaction) {
        homeFragment?.let {
            trasaction.hide(homeFragment!!)
        }
        knowledgeFragment?.let {
            trasaction.hide(knowledgeFragment!!)
        }
        weChatFragment?.let {
            trasaction.hide(weChatFragment!!)
        }
        navigationFragment?.let {
            trasaction.hide(navigationFragment!!)
        }
        projectFragment?.run {
            trasaction.hide(projectFragment!!)
        }
    }

    override fun initData() {
        EventViewModel.themeColorLiveData.value= ThemeEvent(SettingUtil.getColor())

    }

    @Subscribe
    fun onEvent(event: LoginEvent){
        if(event.islogin){
            tv_username?.text=userName
            nav_view?.menu?.findItem(R.id.nav_logout)?.isVisible=true
            getUserInfo()
        }else{
            tv_username?.text="去登录"
            tv_user_grade?.text="--"
            tv_user_rank?.text="--"
            nav_view?.menu?.findItem(R.id.nav_logout)?.isVisible=false
        }
    }

    private fun getUserInfo() {
        HttpRetrofit.apiService.getUserInfo().applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<UserInfoBody>>{
            override fun onSuccess(t: HttpResult<UserInfoBody>) {
                t.data?.let{
                    SharedPreUtil.saveString(this@MainActivity,Constant.USER_INFO,Gson().toJson(t.data))
                    tv_user_rank?.text= it.rank.toString()
                    tv_user_grade?.text=it.level.toString()
                }
            }
            override fun onFailture(t: Throwable) {

            }
        }))

    }
}
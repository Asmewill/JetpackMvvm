package com.example.oapp.ui


import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.base.BaseActivity
import com.example.oapp.constant.Constant
import com.example.oapp.ext.showToast
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.AgentWeb


/**
 * Created by jsxiaoshui on 2021/6/29
 */
@Route(path = Constant.PagePath.CONTENT)
class ContentActivity:BaseActivity() {
    @Autowired
    @JvmField var  title:String?=null
    @Autowired
    @JvmField  var id: Int=0
    @Autowired
    @JvmField  var url: String?=null

    private lateinit var agentWeb:AgentWeb
    lateinit var toolbar: Toolbar
    lateinit var tv_title: TextView
    lateinit var cl_main:CoordinatorLayout
    override fun attachLayoutRes(): Int {
        return R.layout.activity_content
    }

    override fun initView() {
        toolbar=findViewById<Toolbar>(R.id.toolbar)
        tv_title=findViewById<TextView>(R.id.tv_title)
        cl_main=findViewById<CoordinatorLayout>(R.id.cl_main)

        ARouter.getInstance().inject(this)// Start auto inject.
        toolbar?.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)//显示系统的返回键
        }
        setTitle(title)
        tv_title.setText(getString(R.string.agentweb_loading))
        tv_title.visibility=View.VISIBLE
        tv_title.postDelayed({
               tv_title.isSelected=true
            }
        ,2000)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home->{//系统的返回键
                finish()
            }
            R.id.action_like->{
                showToast("like")
            }
            R.id.action_share->{
                showToast("share")
            }
            R.id.action_browser->{
                showToast("browser")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {
        var layoutParams=CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,CoordinatorLayout.LayoutParams.MATCH_PARENT)
         layoutParams.behavior=AppBarLayout.ScrollingViewBehavior()
         agentWeb = AgentWeb.with(this@ContentActivity)
             .setAgentWebParent(cl_main, layoutParams)
             .useDefaultIndicator()
             .setWebChromeClient(MyWebChromeClient(true,this@ContentActivity))
 //            .setWebChromeClient(webChromeClient)
//             .setWebView(NestedScrollAgentWebView(this@ContentActivity))//指定webview为NestedScrollAgentWebView，toolbar可以显示隐藏
             .createAgentWeb().ready().go(url)

    }

    //第一种方式，直接创建常量   对象表达式
    private val webChromeClient =object:WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            title?.apply {
                tv_title.setText(title)
            }
        }
    }

    //第一种方式，使用内部类方式
    private class MyWebChromeClient(private var success:Boolean,private var activity:ContentActivity):WebChromeClient(){

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            if(success){
                title?.apply {
                    activity.tv_title.setText(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }


}
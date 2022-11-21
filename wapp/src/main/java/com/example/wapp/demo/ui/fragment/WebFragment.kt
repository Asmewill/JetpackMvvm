package com.example.wapp.demo.ui.fragment

import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.example.oapp.base.BaseViewModel
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentWebBinding
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.viewmodel.CollectViewModel
import com.hyphenate.easeui.widget.EaseAlertDialog
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_web.*

/**
 * Data :2022/5/26
 * Time:14:57
 * Author:shuij
 *
 */
class WebFragment: BaseVmDbFragment<BaseViewModel, FragmentWebBinding>() {
    private val collectViewModel:CollectViewModel by viewModels()

    private var mAgentWeb:AgentWeb?=null
    private var preWeb:AgentWeb.PreAgentWeb?=null

    private var articleId =-1
    private var articleTitle=""
    private var isCollect=false
    private var url=""
    private var collectType=CollectType.Article.type

    override fun layoutId(): Int {
        return R.layout.fragment_web
    }

    override fun initView() {
        setHasOptionsMenu(true)
        arguments?.let {
            articleTitle=it.getString(Constant.ARTICLE_TITLE,"")
            url=it.getString(Constant.URL,"")
            isCollect=it.getBoolean(Constant.IS_COLLECT)
            collectType=it.getInt(Constant.COLLECT_TYPE)
        }
        mActivity.setSupportActionBar(toolbar)
        toolbar.initClose(titleStr = articleTitle, onBack = {
            if(mAgentWeb!!.webCreator.webView.canGoBack()){
                mAgentWeb!!.webCreator.webView.goBack()
            }else{
                nav().navigateUp()
            }
        })
        preWeb = AgentWeb.with(this)
            .setAgentWebParent(webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
    }

    override fun initData() {
        mAgentWeb=preWeb?.go(url)
        requireActivity().onBackPressedDispatcher.addCallback(this,object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(mAgentWeb!!.webCreator.webView.canGoBack()){
                    mAgentWeb!!.webCreator.webView.goBack()
                }else{
                    nav().navigateUp()
                }
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        //如果收藏了，右上角的图标相对应改变
        context?.let {
            if (isCollect) {
                menu.findItem(R.id.web_collect).icon =
                    ContextCompat.getDrawable(it, R.drawable.ic_collected)
            } else {
                menu.findItem(R.id.web_collect).icon =
                    ContextCompat.getDrawable(it, R.drawable.ic_collect)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }


    override fun createObserver() {
        collectViewModel.collectLiveData.observe(this, Observer {
           when(it.isSuccess){
               true->{
                   if(it.response!!.getStatus()){
                       isCollect=true
                       mActivity.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                       mActivity.invalidateOptionsMenu()
                   }else{
                       it.errorMsg?.let {
                           ToastUtils.showLong(it)
                       }
                   }
               }
               false->{
                   it.errorMsg?.let {
                       ToastUtils.showLong(it)
                   }
               }
           }

        })
        collectViewModel.collectUrlLiveData.observe(this, Observer {
            when(it.isSuccess){
                true->{
                    if(it.response!!.getStatus()){
                        isCollect=true
                        mActivity.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                        mActivity.invalidateOptionsMenu()
                    }else{
                        it.errorMsg?.let {
                            ToastUtils.showLong(it)
                        }
                    }
                }
                false->{
                    it.errorMsg?.let {
                        ToastUtils.showLong(it)
                    }
                }
            }
        })
        collectViewModel.unCollectLiveData.observe(this, Observer {
            when(it.isSuccess){
                true->{
                    if(it.response!!.getStatus()){
                        isCollect=false
                        mActivity.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                        mActivity.invalidateOptionsMenu()
                    }else{
                        it.errorMsg?.let {
                            ToastUtils.showLong(it)
                        }
                    }
                }
                false->{
                    it.errorMsg?.let {
                        ToastUtils.showLong(it)
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.web_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.web_collect->{
                VibrateUtils.vibrate(500)
                if(CacheUtil.isLogin()){
                    if(!isCollect){
                        if(collectType==CollectType.Article.type){
                            collectViewModel.collectArticle(articleId)
                        }else if(collectType==CollectType.Url.type){
                            collectViewModel.collectUrl(articleTitle,url)
                        }
                    }else{
                        collectViewModel.unCollectArticle(articleId)
                    }
                }else{
                    nav().navigate(R.id.action_WEbFragment_to_LoginFragment)
                }
            }
            R.id.web_share->{
                val intent=Intent();
                intent.action=Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT,articleTitle+":"+url)
                intent.type="text/plain"
                val shareIntent=Intent.createChooser(intent,"分享到")
                startActivity(shareIntent)
            }
            R.id.web_refresh->{
                mAgentWeb?.run {
                    urlLoader.reload()
                }
            }
            R.id.web_liulanqi->{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        mAgentWeb?.webLifeCycle?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mAgentWeb?.webLifeCycle?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb?.webLifeCycle?.onDestroy()
    }
}
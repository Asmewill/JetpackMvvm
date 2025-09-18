package com.example.oapp.ui.fragment

import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.example.oapp.R
import com.example.oapp.adapter.WeChatPageAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.WeChatData
import com.example.oapp.event.ThemeEvent
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.utils.SettingUtil
import com.example.oapp.viewmodel.EventViewModel
import com.google.android.material.tabs.TabLayout
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021/6/25
 */
class WeChatFragment:BaseFragment() {
    lateinit var loadService: LoadService<Any>

    lateinit var ll_content: LinearLayout
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun attachlayoutRes(): Int {
        return R.layout.fragment_wechat
    }

    override fun initView() {
        ll_content=mContentView.findViewById<LinearLayout>(R.id.ll_content)
        tabLayout=mContentView.findViewById<TabLayout>(R.id.tabLayout)
        viewPager=mContentView.findViewById<ViewPager>(R.id.viewPager)
        createObserver()
        //注册LoadingService
        loadService = LoadSir.getDefault().register(ll_content) {
            loadService.showCallback(LoadingCallback::class.java)
            getWechatTab()
        }

    }

    private fun createObserver() {
        EventViewModel.themeColorLiveData.observeInFragment(this){
            if(!SettingUtil.getIsNightMode()){
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    override fun initData() {
        //通知更新Tablayout的颜色
        EventViewModel.themeColorLiveData.value= ThemeEvent(SettingUtil.getColor())
        loadService.showCallback(LoadingCallback::class.java)
        getWechatTab()
    }

    private fun getWechatTab() {
        HttpRetrofit.apiService.getWechatTab().applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<List<WeChatData>>>{
            override fun onSuccess(t: HttpResult<List<WeChatData>>) {
                loadService.showCallback(SuccessCallback::class.java)
                t.data.let {
                    viewPager.adapter=WeChatPageAdapter(it as ArrayList<WeChatData>,
                       childFragmentManager
                    )
                    tabLayout.setupWithViewPager(viewPager)
                }
            }
            override fun onFailture(error: Throwable) {
                loadService.showCallback(ErrorCallback::class.java)
                error?.message?.let {
                    showToast(it)
                }
            }
        }))
    }
}
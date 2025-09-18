package com.example.oapp.ui.fragment

import android.widget.LinearLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.oapp.R
import com.example.oapp.adapter.ProjectPageAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.ProjectData
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
class ProjectFragment:BaseFragment() {
    lateinit var loadService: LoadService<Any>

    lateinit  var ll_content: LinearLayout
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    //lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    override fun attachlayoutRes(): Int {
        return R.layout.fragment_project
    }

    override fun initView() {
        ll_content=mContentView.findViewById<LinearLayout>(R.id.ll_content)
        tabLayout=mContentView.findViewById<TabLayout>(R.id.tabLayout)
        viewPager=mContentView.findViewById<ViewPager>(R.id.viewPager)
       // swipeRefreshLayout=mContentView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        createObserver()
        //注册LoadingService
        loadService = LoadSir.getDefault().register(ll_content) {
            loadService.showCallback(LoadingCallback::class.java)
            getProjectTab()
        }
    }

    override fun initData() {
        //通知更新Tablayout的颜色
        EventViewModel.themeColorLiveData.value= ThemeEvent(SettingUtil.getColor())
        loadService.showCallback(LoadingCallback::class.java)
        getProjectTab()

    }

    private fun getProjectTab() {
        HttpRetrofit.apiService.getProjectTab().applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<List<ProjectData>>>{
            override fun onSuccess(t: HttpResult<List<ProjectData>>) {
                loadService.showCallback(SuccessCallback::class.java)
                viewPager.adapter=ProjectPageAdapter(t.data as ArrayList<ProjectData>,childFragmentManager)
                tabLayout.setupWithViewPager(viewPager)
            }
            override fun onFailture(error: Throwable) {
                //swipeRefreshLayout?.isRefreshing=false
                loadService.showCallback(ErrorCallback::class.java)
                error?.message?.let {
                    showToast(it)
                }
            }
        }))

    }
    private fun createObserver() {
        EventViewModel.themeColorLiveData.observeInFragment(this){
            if(!SettingUtil.getIsNightMode()){
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }
}
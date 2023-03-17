package com.example.oapp.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oapp.R
import com.example.oapp.adapter.NavRightAdapter
import com.example.oapp.adapter.NavTabAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.NavBean
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_navigation.recyclerView
import kotlinx.android.synthetic.main.fragment_wechat_tab.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

/**
 * Created by jsxiaoshui on 2021/6/25
 */
class NavigationFragment:BaseFragment() {
    lateinit var loadService: LoadService<Any>
    private var bScroll: Boolean=false
    private var currentPositon: Int=0
    private val layoutManager by lazy {
      LinearLayoutManager(activity)
    }
    private val navRightAdapter by lazy {
        NavRightAdapter()
    }
    override fun attachlayoutRes(): Int {
        return R.layout.fragment_navigation
    }

    override fun initView() {
        //注册LoadingService
        loadService = LoadSir.getDefault().register(normal_view) {
            loadService.showCallback(LoadingCallback::class.java)
            getNavData()
        }
        recyclerView?.let {
            it.layoutManager=layoutManager
            it.adapter=navRightAdapter
        }
        navigation_tab_layout.addOnTabSelectedListener(object:VerticalTabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabView?, position: Int) {
                recyclerView.stopScroll()
                currentPositon=position
                smothToPosition(position)
            }
            override fun onTabReselected(tab: TabView?, position: Int) {


            }
        })
        recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(bScroll&&(newState==RecyclerView.SCROLL_STATE_IDLE)){
                    scrollRecyclerView()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(bScroll){
                    scrollRecyclerView()
                }
            }
        })
    }

    private fun scrollRecyclerView() {
        bScroll=false
        val index=currentPositon-layoutManager.findFirstVisibleItemPosition()
        if(index>0&&index<recyclerView.childCount){
            val top=recyclerView.getChildAt(index).top
            recyclerView.scrollTo(0,top)
        }
    }


    private fun smothToPosition(position: Int) {
        val firstPosition=layoutManager.findFirstVisibleItemPosition()
        val lastPosition=layoutManager.findLastVisibleItemPosition()
        when{
            (position<=firstPosition)->{
                recyclerView.smoothScrollToPosition(position)
            }
            (position<=lastPosition)->{
                val top=recyclerView.getChildAt(position-firstPosition).top
                recyclerView.smoothScrollBy(0,top)
            }
            else->{
                recyclerView.smoothScrollToPosition(position)
                bScroll = true
            }
        }
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        getNavData()
    }

    private fun getNavData() {
        HttpRetrofit.apiService.getNavTab().applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<List<NavBean>>>{
            override fun onSuccess(t: HttpResult<List<NavBean>>) {
                loadService.showCallback(SuccessCallback::class.java)
                t.data?.let {
                    navigation_tab_layout.setTabAdapter(NavTabAdapter(activity!!,
                       it as ArrayList<NavBean>
                    ))
                    navRightAdapter.setNewData(it)
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
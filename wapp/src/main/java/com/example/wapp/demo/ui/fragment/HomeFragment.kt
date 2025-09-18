package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentHomeBinding
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.adapter.HomeBannerAdapter
import com.example.wapp.demo.adapter.HomeBannerViewHolder
import com.example.wapp.demo.bean.ArticleResponse
import com.example.wapp.demo.bean.BannerResponse
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initFloatBtn
import com.example.wapp.demo.ext.initFooter
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.http.apiService
import com.example.wapp.demo.viewmodel.HomeViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.zhpan.bannerview.BannerViewPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import com.example.wapp.demo.loadcallback.ErrorCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import java.lang.Exception
import java.util.*

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class HomeFragment : BaseVmDbFragment<HomeViewModel, FragmentHomeBinding>() ,CoroutineScope by MainScope() {

    private lateinit var footView: DefineLoadMoreView
    private lateinit var headerView:View
    private val articleAdapter by lazy {
        AriticleAdapter(arrayListOf(), true)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_home

    }

    override fun initView() {



        //注册LoadingService
        loadService = LoadSir.getDefault().register(mDataBind.swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getBannerData()
            mViewModel.getHomeData(true)
        }
        mDataBind.toolbar?.run {
            this.init("首页")
            this.inflateMenu(R.menu.home_menu)
            this.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        // NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.act_Main_to_SearFragment)
                        nav().navigate(R.id.action_Main_to_SearchFragment)
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
        //初始化RecycleView
        mDataBind.recyclerView.init(LinearLayoutManager(activity), articleAdapter).let {
             it.initFooter(SwipeRecyclerView.LoadMoreListener {
                mViewModel.getHomeData(false)
            })
            it.initFloatBtn(mDataBind.floatbtn)
        }
        //初始化SwipeRefreshLayout
        mDataBind.swipeRefresh.init {
            mViewModel.getHomeData(true)
        }
        articleAdapter.run {
            this.setOnItemClickListener { adapter, view, position ->
                val item=adapter.data.get(position-1) as ArticleResponse
                val bundle=Bundle()
                bundle.putString(Constant.ARTICLE_TITLE,item.title)
                bundle.putString(Constant.URL,item.link)
                bundle.putInt(Constant.ARTICLE_ID,item.id)
                bundle.putInt(Constant.COLLECT_TYPE,CollectType.Article.type)
                bundle.putBoolean(Constant.IS_COLLECT,false)
                //try-catch防止重复点击导致跳转多次
                try {
                    nav().navigate(R.id.action_Main_to_WebFragment,bundle)
                }catch (e:Exception){}
            }
        }
    }

    override fun lazyLoad() {
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getBannerData()
        mViewModel.getHomeData(true)
    }

    override fun initData() {

    }

    override fun createObserver() {
        mViewModel.bannerDataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {listDataUistate->
             if(mDataBind.recyclerView.headerCount==0){
                  headerView=LayoutInflater.from(context).inflate(R.layout.include_banner,null).apply {
                    this.findViewById<BannerViewPager<BannerResponse,HomeBannerViewHolder>>(R.id.banner_view).apply {
                        this.adapter=HomeBannerAdapter()
                        this.setLifecycleRegistry(lifecycle)
                        this.setOnPageClickListener {position->
                            val item=listDataUistate.listData.get(position)
                            val bundle=Bundle()
                            bundle.putString(Constant.ARTICLE_TITLE,item.title)
                            bundle.putString(Constant.URL,item.url)
                            bundle.putInt(Constant.ARTICLE_ID,item.id)
                            bundle.putInt(Constant.COLLECT_TYPE,CollectType.Article.type)
                            bundle.putBoolean(Constant.IS_COLLECT,false)
                            nav().navigate(R.id.action_Main_to_WebFragment,bundle)
                        }
                        create(listDataUistate.listData)
                    }
                 }
                 mDataBind.recyclerView.addHeaderView(headerView)
                 mDataBind.recyclerView.scrollToPosition(0)
             }
        })
        mViewModel.homeDataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mDataBind.swipeRefresh.isRefreshing=false
            mDataBind.recyclerView.loadMoreFinish(it.listData.isEmpty(),it.hasMore)
            if(it.isSuccess){
                when{
                    //第一页，没有数据，显示空布局
                    it.isEmpty->{
                        loadService.showCallback(EmptyCallback::class.java)
                    }
                    //第一页，有数据
                    it.isRefresh->{
                        articleAdapter.setList(it.listData)
                        loadService.showCallback(SuccessCallback::class.java)
                    }
                    //不是第一页
                    else ->{
                        articleAdapter.addData(it.listData)
                    }
                }
            }else{
                //如果是第一的时候，网络异常了
                if(it.isRefresh){
                    if(mDataBind.recyclerView.headerCount>0){
                        mDataBind.recyclerView.removeHeaderView(headerView)
                    }
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{//第二页，第三页...异常
                    //上啦加载网络异常
                    mDataBind.recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })
    }
    var job1:Job? =null;
    fun testXieChengRequest(){
         job1 =  GlobalScope.launch() {
            runCatching {
                //login是个suspend函数
                val result = apiService.getBanner()
            }.onSuccess {

            }.onFailure {

            }
        }

        GlobalScope.async {

        }
        //在主线程上执行 (Dispatchers.Main)。
        //使用 SupervisorJob 来管理生命周期和错误处理
        MainScope().launch {//此协程块默认是在UI线程中启动协程

        }
        mViewModel.viewModelScope.launch(Dispatchers.Default) {

        }
        mViewModel.viewModelScope.launch(Dispatchers.Main) {

        }

        lifecycleScope.launch{

        }

        var job:Job=CoroutineScope(Dispatchers.IO).async {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
         job1?.cancel() //取消任务
         job1=null //清空引用，有助于避免内存泄漏

    }

}
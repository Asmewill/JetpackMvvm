package com.example.oapp.ui.fragment

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.bingoogolapple.bgabanner.BGABanner
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.adapter.HomeAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.Banner
import com.example.oapp.bean.HomeData
import com.example.oapp.bean.HttpResult
import com.example.oapp.constant.Constant
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.ui.ContentActivity
import com.example.oapp.utils.CommonUtil
import com.example.oapp.utils.ImageLoader
import com.example.oapp.utils.SettingUtil
import com.example.oapp.viewmodel.CollectViewModel
import com.example.oapp.viewmodel.EventViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021/6/25
 */
class HomeFragment : BaseFragment() {
    private val mViewModel: CollectViewModel by viewModels()
    private var topList = ArrayList<HomeData.DatasBean>()
    private var bannerList = ArrayList<Banner>()
    private var headerView: View? = null
    private var banner: BGABanner? = null
    private var pageNo: Int = 0
    lateinit var loadService: LoadService<Any>
    lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    lateinit var  recyclerView:RecyclerView
    private val homeAdapter by lazy {
        HomeAdapter(mViewModel)
    }

    override fun attachlayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        swipeRefreshLayout=mContentView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        recyclerView=mContentView.findViewById<RecyclerView>(R.id.recyclerView);

        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback::class.java)
            pageNo = 0
            getBanners()
        }
        createObserver()
        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            it.adapter = homeAdapter
        }
        //headerView
        headerView = layoutInflater.inflate(R.layout.item_home_banner, null)
        banner = headerView?.findViewById<BGABanner>(R.id.banner)
        homeAdapter?.run {
            addHeaderView(headerView)
            this.setOnItemClickListener { baseQuickAdapter, view, i ->
                ARouter.getInstance().build(Constant.PagePath.CONTENT)
                    .withString(Constant.CONTENT_TITLE, topList.get(i).title)
                    .withString(Constant.CONTENT_URL, topList.get(i).link)
                    .withInt(Constant.CONTENT_ID, topList.get(i).id)
                    .navigation()
            }
        }
        banner?.setDelegate(object : BGABanner.Delegate<ImageView, String> {
            override fun onBannerItemClick(p0: BGABanner?, p1: ImageView?, p2: String?, p3: Int) {
                ARouter.getInstance().build(Constant.PagePath.CONTENT)
                    .withString(Constant.CONTENT_TITLE, bannerList.get(p3).title)
                    .withString(Constant.CONTENT_URL, bannerList.get(p3).url)
                    .withInt(Constant.CONTENT_ID, bannerList.get(p3).id)
                    .navigation()
            }
        })

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener {
            pageNo = 0
            getBanners()
        }
        //上拉加载
        homeAdapter.setOnLoadMoreListener({
            pageNo++
            getTopList()
        }, recyclerView)

    }

    private fun createObserver() {
        EventViewModel.noPhotoLiveData.observeInFragment(this) {
            pageNo = 0
            getBanners()
        }
        EventViewModel.showTopArticleLiveData.observeInFragment(this) {
            pageNo = 0
            getBanners()
        }
        mViewModel.addCollectLiveData.observe(this, Observer {
            when (!it.isException) {
                true -> {
                    if (it.dataBean?.errorCode == 0) {
                        CommonUtil.showToast("收藏成功")
                        homeAdapter.getItem(it.position)?.collect = true
                        homeAdapter.notifyDataSetChanged()
                    } else {
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false -> {
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })

        mViewModel.cancelCollectLiveData.observe(this, Observer {
            when (!it.isException) {
                true -> {
                    if (it.dataBean?.errorCode == 0) {
                        CommonUtil.showToast("已取消收藏")
                        homeAdapter.getItem(it.position)?.collect = false
                        homeAdapter.notifyDataSetChanged()
                    } else {
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false -> {
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        getBanners()
    }

    //只有interface才能使用object
    fun getBanners() {
        HttpRetrofit.apiService.getBanners().applySchdules()
            .subscribe(OObserver(object : ApiCallback<HttpResult<List<Banner>>> {
                override fun onSuccess(t: HttpResult<List<Banner>>) {
                    swipeRefreshLayout?.isRefreshing = false
                    bannerList = t.data as ArrayList<Banner>
                    var titleList = arrayListOf<String>()
                    var urlList = arrayListOf<String>()
                    t?.data?.forEach {
                        titleList.add(it.title + "")
                        urlList.add(it.imagePath + "")
                    }
                    banner?.let {
                        it.setAutoPlayAble(urlList.size > 1)
                        it.setData(urlList, titleList)
                        it.setAdapter(object : BGABanner.Adapter<ImageView, String> {
                            override fun fillBannerItem(
                                p0: BGABanner?,
                                p1: ImageView?,
                                p2: String?,
                                p3: Int
                            ) {
                                //loadIv的三个参数都不能为null,所以....
                                activity?.let { it1 ->
                                    p1?.let { it2 ->
                                        p2?.let { it3 ->
                                            ImageLoader.loadIv(
                                                it1,
                                                it3, it2
                                            )
                                        }
                                    }
                                }
                            }
                        })
                    }
                    //顺序调用
                    getTopList()
                }

                override fun onFailture(t: Throwable) {
                    swipeRefreshLayout?.isRefreshing = false
                    //如果无网络，或者异常
                    getTopList()
                }
            }))

    }

    private fun getTopList() {
        //第一页
        if (pageNo == 0) {
            //是否显示置顶文章
            if (SettingUtil.getIsShowTopArticle()) {
                HttpRetrofit.apiService.getTopArticles().applySchdules()
                    .subscribe(OObserver(object :
                        ApiCallback<HttpResult<ArrayList<HomeData.DatasBean>>> {
                        override fun onSuccess(t: HttpResult<ArrayList<HomeData.DatasBean>>) {
                            t?.data?.let {
                                topList = it
                                topList?.forEach {
                                    it.isTop = true
                                }
                            }
                            requestList()
                        }

                        override fun onFailture(t: Throwable) {
                            //如果无网络，或者异常
                            requestList()

                        }
                    }))

            } else {
                topList?.clear()
                requestList()
            }
        } else {  //第二三...页
            requestList()
        }
    }

    private fun requestList() {
        HttpRetrofit.apiService.getArticles(pageNo).applySchdules()
            .subscribe(OObserver(object : ApiCallback<HttpResult<HomeData>> {
                override fun onSuccess(dataBean: HttpResult<HomeData>) {
                    dataBean.data?.datas?.let {
                        if (dataBean.data != null && dataBean.data?.datas != null &&
                            dataBean.data?.datas?.size ?: 0 > 0) {//数据为null
                            if (pageNo <= 0) {//第一页
                                loadService.showCallback(SuccessCallback::class.java)
                                dataBean?.data?.datas?.let { beanList ->
                                    if (topList != null && topList.size >= 0) {
                                        topList.addAll(beanList)
                                        homeAdapter.setNewData(topList)
                                    } else {
                                        homeAdapter.setNewData(beanList)
                                    }
                                }
                            } else {//第二三....页
                                dataBean?.data?.datas?.let { beanList ->
                                    homeAdapter.addData(beanList)
                                }
                                //强制停止RecycleView的滑动
                                recyclerView?.let {
                                    CommonUtil.forceStopRecycleViewScroll(it)
                                }
                            }
                            //设置是否可以加载更多
                            if (dataBean?.data?.curPage!! < dataBean?.data?.pageCount!!) {
                                homeAdapter.loadMoreComplete()
                                homeAdapter.setEnableLoadMore(true)
                            } else {
                                homeAdapter.loadMoreEnd(false)
                            }
                        } else {//数据不为null
                            if (pageNo <= 0) {//第一页,显示空布局
                                loadService.showCallback(EmptyCallback::class.java)
                            }
                        }
                    }
                }
                override fun onFailture(error: Throwable) {
                    if (pageNo > 0) {
                        homeAdapter.loadMoreFail()//上拉加载,第二页之后，加载失败时显示异常
                        pageNo--
                    } else {
                        loadService.showCallback(ErrorCallback::class.java)
                    }
                    error?.message?.let {
                        showToast(it)
                    }
                }
            }))
    }


}
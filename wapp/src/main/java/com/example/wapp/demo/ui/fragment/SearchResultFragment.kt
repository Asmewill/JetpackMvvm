package com.example.wapp.demo.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSearchResultBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.ext.*
import com.example.wapp.demo.viewmodel.SearchResultViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.floatbtn
import kotlinx.android.synthetic.main.fragment_home.recyclerView
import kotlinx.android.synthetic.main.fragment_home.swipeRefresh
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-10-25
 */
class SearchResultFragment :
    BaseVmDbFragment<SearchResultViewModel, FragmentSearchResultBinding>() {
    lateinit var loadService: LoadService<Any>
    private val articleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }
    private var searchKey = ""
    override fun layoutId(): Int {
        return R.layout.fragment_search_result
    }

    override fun initView() {
        searchKey = arguments?.getString("searchKey").toString()
        toolbar.initClose(searchKey) {
            nav().navigateUp()
        }
        rv_search_list.layoutManager = LinearLayoutManager(mActivity)
        rv_search_list.adapter = articleAdapter
        val footView = DefineLoadMoreView(MyApp.instance)
        //设置尾部点击
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            footView.onLoading()
            mViewModel.getSearchDataByKey(searchKey, false)
        })
        rv_search_list.addFooterView(footView)
        rv_search_list.setLoadMoreView(footView)
        rv_search_list.setLoadMoreListener {
            mViewModel.getSearchDataByKey(searchKey, false)
        }
        rv_search_list.initFloatBtn(floatbtn)
        swipeRefresh.init {
            mViewModel.getSearchDataByKey(searchKey, true)
        }
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getSearchDataByKey(searchKey, true)
        }

    }

    override fun initData() {
        mViewModel.getSearchDataByKey(searchKey, true)
    }

    override fun createObserver() {
        mViewModel.searchResultLiveData.observe(mActivity, Observer {
            swipeRefresh.isRefreshing = false
            if (it.isSuccess) {
                if (it.isRefresh && it.listData.size <= 0) {//空布局
                    loadService.showCallback(EmptyCallback::class.java)
                } else if (it.isRefresh && it.listData.size > 0) {
                    articleAdapter.setList(it.listData)
                    loadService.showCallback(SuccessCallback::class.java)
                } else {
                    articleAdapter.addData(it.listData)
                    loadService.showCallback(SuccessCallback::class.java)
                }
                rv_search_list.loadMoreFinish(it.isEmpty, it.hasMore)//必须要加，否则上拉加载，无法实现
            } else {//异常数据
                if (it.isRefresh) {
                    loadService.showCallback(ErrorCallback::class.java)
                } else {
                    rv_search_list.loadMoreError(404, it.errorMsg)
                }
            }
        })
    }
}
package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSearchResultBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.bean.ArticleResponse
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.*
import com.example.wapp.demo.loadcallback.ErrorCallback
import com.example.wapp.demo.viewmodel.SearchResultViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-10-25
 */
class SearchResultFragment : BaseVmDbFragment<SearchResultViewModel, FragmentSearchResultBinding>() {
    private val articleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }
    private var searchKey = ""
    override fun layoutId(): Int {
        return R.layout.fragment_search_result
    }

    override fun initView() {
        //注册LoadingService
        loadService = LoadSir.getDefault().register(mDataBind.swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getSearchDataByKey(searchKey, true)
        }
        searchKey = arguments?.getString("searchKey").toString()
        mDataBind.toolbar.initClose(searchKey) {
            nav().navigateUp()
        }
        mDataBind.rvSearchList.layoutManager = LinearLayoutManager(mActivity)
        mDataBind.rvSearchList.adapter = articleAdapter
        val footView = DefineLoadMoreView(MyApp.instance)
        //设置尾部点击
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            footView.onLoading()
            mViewModel.getSearchDataByKey(searchKey, false)
        })
         mDataBind.rvSearchList.addFooterView(footView)
        mDataBind.rvSearchList.setLoadMoreView(footView)
        mDataBind.rvSearchList.setLoadMoreListener {
            mViewModel.getSearchDataByKey(searchKey, false)
        }
        mDataBind.rvSearchList.initFloatBtn(mDataBind.floatbtn)
        mDataBind.swipeRefresh.init {
            mViewModel.getSearchDataByKey(searchKey, true)
        }
        articleAdapter.setOnItemClickListener { adapter, view, position ->
            val item=adapter.data.get(position) as ArticleResponse
            val bundle= Bundle()
            bundle.putString(Constant.ARTICLE_TITLE,item.title)
            bundle.putString(Constant.URL,item.link)
            bundle.putInt(Constant.ARTICLE_ID,item.id)
            bundle.putInt(Constant.COLLECT_TYPE, CollectType.Article.type)
            bundle.putBoolean(Constant.IS_COLLECT,false)
            try {
                nav().navigate(R.id.action_SearchResultFragment_to_WebFragment,bundle)
            }catch (e:Exception){
            }
        }
    }

    override fun initData() {
        mViewModel.getSearchDataByKey(searchKey, true)
    }

    override fun createObserver() {
        mViewModel.searchResultLiveData.observe(mActivity, Observer {
            mDataBind.swipeRefresh.isRefreshing = false
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
                mDataBind.rvSearchList.loadMoreFinish(it.isEmpty, it.hasMore)//必须要加，否则上拉加载，无法实现
            } else {//异常数据
                if (it.isRefresh) {
                    loadService.showCallback(ErrorCallback::class.java)
                } else {
                    mDataBind.rvSearchList.loadMoreError(404, it.errorMsg)
                }
            }
        })
    }
}
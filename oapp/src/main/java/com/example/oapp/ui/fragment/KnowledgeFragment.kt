package com.example.oapp.ui.fragment

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.adapter.KnowledgeAdapter
import com.example.oapp.base.BaseFragment
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.KnowledgeData
import com.example.oapp.constant.Constant
import com.example.oapp.ext.applySchdules
import com.example.oapp.ext.showToast
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.ui.KnowledgeActivity
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_knowledge.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021/6/25
 */
class KnowledgeFragment:BaseFragment() {
    lateinit var loadService: LoadService<Any>
    private val knowledgeAdapter by lazy {
        KnowledgeAdapter()
    }

    override fun attachlayoutRes(): Int {
        return R.layout.fragment_knowledge
    }

    override fun initView() {
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback::class.java)
            getKnowledgeTree()
        }
        recyclerView?.run {
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            adapter=knowledgeAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            getKnowledgeTree()
        }
        knowledgeAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            ARouter.getInstance().build(Constant.PagePath.KNOWLEDGE)
                .withSerializable(Constant.ITEM_BENA,baseQuickAdapter.data.get(i) as KnowledgeData)
                .navigation()
        }
    }

    override fun initData() {
        loadService.showCallback(LoadingCallback::class.java)
        getKnowledgeTree()
    }

    private fun getKnowledgeTree() {
        HttpRetrofit.apiService.getKnowledgeTree().applySchdules()
            .subscribe(OObserver(object:ApiCallback<HttpResult<List<KnowledgeData>>>{
            override fun onSuccess(t: HttpResult<List<KnowledgeData>>) {
                loadService.showCallback(SuccessCallback::class.java)
                t.data?.run {
                    knowledgeAdapter.setNewData(t.data)
                }
                swipeRefreshLayout?.isRefreshing=false
            }
            override fun onFailture(error: Throwable) {
                swipeRefreshLayout?.isRefreshing=false
                loadService.showCallback(ErrorCallback::class.java)
                error?.message?.let {
                    showToast(it)
                }
            }
        }))
    }
}
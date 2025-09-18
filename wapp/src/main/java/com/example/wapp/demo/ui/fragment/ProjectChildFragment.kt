package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentProjectChildBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.adapter.AriticleAdapter
import com.example.wapp.demo.bean.ArticleResponse
import com.example.wapp.demo.bean.enums.CollectType
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.viewmodel.ProjectViewModel
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import com.example.wapp.demo.loadcallback.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback

/**
 * Created by jsxiaoshui on 2021-11-03
 */
class ProjectChildFragment : BaseVmDbFragment<ProjectViewModel, FragmentProjectChildBinding>() {
    private var cid = 0
    private var isNew = false
    private val articleAdapter by lazy {
       AriticleAdapter(mutableListOf())
    }
    private val footView by lazy {
        DefineLoadMoreView(MyApp.instance)
    }
    companion object {
        fun newInstance(cid: Int, isNew: Boolean): ProjectChildFragment {
            val instance = ProjectChildFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.CID, cid)
            bundle.putBoolean(Constant.IS_NEW, isNew)
            instance.arguments = bundle
            return instance
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_project_child
    }

    override fun initView() {
        arguments?.let {
            cid = it.getInt(Constant.CID, 0)
            isNew = it.getBoolean(Constant.IS_NEW)
        }
        //注册LoadingService
        loadService = LoadSir.getDefault().register(mDataBind.swipeRefresh) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getProjectData(isRefresh = true,cid = cid,isNew = isNew)
        }
        mDataBind.recyclerView.layoutManager=LinearLayoutManager(mActivity)
        mDataBind.recyclerView.setHasFixedSize(true)
        mDataBind.recyclerView.adapter=articleAdapter
        mDataBind.recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(-1)) {
                    mDataBind.floatbtn.visibility = View.INVISIBLE
                }
            }
        })
        mDataBind.floatbtn.setOnClickListener{
            val layoutManager= mDataBind.recyclerView.layoutManager as LinearLayoutManager
            if(layoutManager.findLastVisibleItemPosition()>=40){
                mDataBind.recyclerView.scrollToPosition(0) //没有滚动动画，快速滑动到底部
            }else{
                mDataBind.recyclerView.smoothScrollToPosition(0)//有滚动动画，快速滑动到底部
            }
        }

        //给recycleView添加footview
        mDataBind.recyclerView.addFooterView(footView)
        mDataBind.recyclerView.setLoadMoreView(footView)
        //设置上拉加载更多
        mDataBind.recyclerView.setLoadMoreListener {
            mViewModel.getProjectData(isRefresh = false,cid = cid,isNew = isNew)
        }
        //异常时,点击footView,获取更多数据
        footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            mViewModel.getProjectData(isRefresh = false,cid = cid,isNew = isNew)
        })
        mDataBind.swipeRefresh.setOnRefreshListener {
            mViewModel.getProjectData(isRefresh = true,cid = cid,isNew = isNew)
        }

        articleAdapter.setOnItemClickListener { adapter, view, position ->
            val item=adapter.data.get(position) as ArticleResponse
            val bundle=Bundle()
            bundle.putString(Constant.ARTICLE_TITLE,item.title)
            bundle.putString(Constant.URL,item.link)
            bundle.putInt(Constant.ARTICLE_ID,item.id)
            bundle.putInt(Constant.COLLECT_TYPE, CollectType.Article.type)
            bundle.putBoolean(Constant.IS_COLLECT,false)
            nav().navigate(R.id.action_Main_to_WebFragment,bundle)
        }
    }

    override fun lazyLoad() {
        super.lazyLoad()
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getProjectData(isRefresh = true,cid = cid,isNew = isNew)
    }

    override fun initData() {

    }

    override fun createObserver() {
        mViewModel.projectLiveData.observe(mActivity, Observer {
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
                    loadService.showCallback(ErrorCallback::class.java)
                    ToastUtils.showShort(it.errorMsg)
                }else{//第二页，第三页...异常
                    //上啦加载网络异常
                    mDataBind.recyclerView.loadMoreError(0,it.errorMsg)
                }
            }
        })
    }
}
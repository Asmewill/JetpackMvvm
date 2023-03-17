package com.example.oapp.ui.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oapp.R
import com.example.oapp.adapter.DoneAdapter
import com.example.oapp.base.BaseVmDbFragment
import com.example.oapp.databinding.FragmentDoneBinding
import com.example.oapp.ext.showToast
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.DoneViewModel
import com.example.oapp.viewmodel.EventViewModel
import com.example.oapp.widget.SwipeItemLayout
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.activity_point_rank_list.*
import kotlinx.android.synthetic.main.fragment_done.*
import kotlinx.android.synthetic.main.fragment_done.recyclerView
import kotlinx.android.synthetic.main.fragment_done.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_todo.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback2

/**
 * Created by jsxiaoshui on 2021/7/27
 */
class DoneFragment:BaseVmDbFragment<DoneViewModel,FragmentDoneBinding>() {
    lateinit var loadService: LoadService<Any>
    private var pageNo: Int=1
    private val doneAdapter by lazy {
        DoneAdapter(mViewModel)
    }
    override fun layoutId(): Int {
        return R.layout.fragment_todo
    }

    override fun createViewModel(): DoneViewModel {
        return ViewModelProvider(this).get(DoneViewModel::class.java)
    }

    override fun initView() {
        //注册LoadingService
        loadService = LoadSir.getDefault().register(swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback2::class.java)
            pageNo=1
            mViewModel.getDoneList(pageNo)
        }
        recyclerView?.let {
            it.layoutManager=LinearLayoutManager(activity)
            it.adapter=doneAdapter
            it.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
        }
        swipeRefreshLayout.setOnRefreshListener {
            pageNo=1
            mViewModel.getDoneList(pageNo)
        }
        doneAdapter.setOnLoadMoreListener({
            //防止不足一页的时候自动加载下一页数据
            if(doneAdapter.data!=null&&doneAdapter.data.size<8){
                return@setOnLoadMoreListener
            }
            pageNo++
            mViewModel.getDoneList(pageNo)
        },recyclerView)
    }
    override fun initData() {
        loadService.showCallback(LoadingCallback2::class.java)
        mViewModel.getDoneList(pageNo)
    }

    override fun createObserver() {
        mViewModel.doneListLiveData.observe(this, Observer {

            swipeRefreshLayout?.isRefreshing = false
            when (!it.isException) {
                true -> {
                    if (it.dataBean != null && it.dataBean?.data?.datas != null &&
                        it.dataBean?.data?.datas?.size ?: 0 > 0) {//数据为null
                        if (pageNo <= 1) {//第一页
                            loadService.showCallback(SuccessCallback::class.java)
                            it.dataBean?.data?.datas?.let { beanList ->
                                doneAdapter.setNewData(beanList)
                            }
                        } else {//第二三....页
                            it.dataBean?.data?.datas?.let { beanList ->
                                doneAdapter.addData(beanList)
                            }
                            //强制停止RecycleView的滑动
                            recyclerView?.let {
                                CommonUtil.forceStopRecycleViewScroll(it)
                            }
                        }
                        //设置是否可以加载更多
                        if (it.dataBean?.data?.curPage!! < it.dataBean?.data?.pageCount!!) {
                            doneAdapter.loadMoreComplete()
                            doneAdapter.setEnableLoadMore(true)
                        } else {
                            doneAdapter.loadMoreEnd(false)
                        }
                    } else {//数据不为null
                        if (pageNo <= 1) {//第一页,显示空布局
                            loadService.showCallback(EmptyCallback::class.java)
                        }
                    }
                }
                false -> {//异常处理
                    if (pageNo > 1) {
                        doneAdapter.loadMoreFail()//上拉加载,第二页之后，加载失败时显示异常
                        pageNo--
                    } else {
                        loadService.showCallback(ErrorCallback::class.java)
                    }
                    it.error?.message?.let { it1 -> showToast(it1) }
                }
            }
        })
        mViewModel.resumeLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("复原成功")
                        EventViewModel.freshLiveData.value=1 ////刷新待办界面
                    }else{
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false->{
                    it.dataBean?.errorMsg?.let {
                        CommonUtil.showToast(it)
                    }
                }
            }
        })
        mViewModel.deleteLiveData.observe(this,Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("删除成功")
                    }else{
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false->{
                    it.dataBean?.errorMsg?.let {
                        CommonUtil.showToast(it)
                    }
                }
            }
        })

        EventViewModel.freshLiveData.observeInFragment(this) {
            if(it==2){
                pageNo = 1
                mViewModel.getDoneList(pageNo)
            }
        }
    }


}
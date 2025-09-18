package com.example.oapp.ui.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oapp.R
import com.example.oapp.adapter.TodoAdapter

import com.example.oapp.base.BaseVmDbFragment
import com.example.oapp.bean.ListDataUiState
import com.example.oapp.bean.ToDoBean
import com.example.oapp.databinding.FragmentTodoBinding
import com.example.oapp.ext.showToast
import com.example.oapp.utils.CommonUtil
import com.example.oapp.utils.SettingUtil
import com.example.oapp.viewmodel.EventViewModel
import com.example.oapp.viewmodel.ToDoViewModel
import com.example.oapp.widget.SwipeItemLayout
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback2


/**
 * Created by jsxiaoshui on 2021/7/27
 */
class ToDoFragment: BaseVmDbFragment<ToDoViewModel, FragmentTodoBinding>() {
    private var pageNo: Int=1
    lateinit var loadService: LoadService<Any>


    private val todoAdapter by lazy {
        TodoAdapter(mViewModel)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_todo
    }
    override fun createViewModel(): ToDoViewModel {
       return ViewModelProvider(this).get(ToDoViewModel::class.java)
    }

    override fun initView() {
        //注册LoadingService
        loadService = LoadSir.getDefault().register(mDataBind.swipeRefreshLayout) {
            loadService.showCallback(LoadingCallback2::class.java)
            pageNo=1
            mViewModel.getTodoList(pageNo)
        }
        mDataBind.recyclerView?.let {
            it.layoutManager=LinearLayoutManager(activity)
            it.adapter=todoAdapter
            //注意必须设置。。。。。。。设置SwipeItemLayout可以左滑
            it.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
        }
        mDataBind.swipeRefreshLayout.setOnRefreshListener {
            pageNo=1
            mViewModel.getTodoList(pageNo)
        }
        todoAdapter.setOnLoadMoreListener({
            //防止不足一页的时候自动加载下一页数据
            if(todoAdapter.data!=null&&todoAdapter.data.size<8){
                      return@setOnLoadMoreListener
            }
            pageNo++
            mViewModel.getTodoList(pageNo)

        },mDataBind.recyclerView)
    }
    override fun initData() {
        loadService.showCallback(LoadingCallback2::class.java)
        mViewModel.getTodoList(pageNo)

    }
    override fun createObserver() {
        mViewModel.toToLiveData.observe(this, Observer<ListDataUiState<ToDoBean>>{
            mDataBind.swipeRefreshLayout?.isRefreshing = false
            when (!it.isException) {
                true -> {
                    if (it.dataBean != null && it.dataBean?.data?.datas != null &&
                        it.dataBean?.data?.datas?.size ?: 0 > 0) {//数据为null
                        if (pageNo <= 1) {//第一页
                            loadService.showCallback(SuccessCallback::class.java)
                            it.dataBean?.data?.datas?.let { beanList ->
                                todoAdapter.setNewData(beanList)
                            }
                        } else {//第二三....页
                            it.dataBean?.data?.datas?.let { beanList ->
                                todoAdapter.addData(beanList)
                            }
                            //强制停止RecycleView的滑动
                            mDataBind.recyclerView?.let {
                                CommonUtil.forceStopRecycleViewScroll(it)
                            }
                        }
                        //设置是否可以加载更多
                        if (it.dataBean?.data?.curPage!! < it.dataBean?.data?.pageCount!!) {
                            todoAdapter.loadMoreComplete()
                            todoAdapter.setEnableLoadMore(true)
                        } else {
                            todoAdapter.loadMoreEnd(false)
                        }
                    } else {//数据不为null
                        if (pageNo <= 1) {//第一页,显示空布局
                            loadService.showCallback(EmptyCallback::class.java)
                        }
                    }
                }
                false -> {//异常处理
                    if (pageNo > 1) {
                        todoAdapter.loadMoreFail()//上拉加载,第二页之后，加载失败时显示异常
                        pageNo--
                    } else {
                        loadService.showCallback(ErrorCallback::class.java)
                    }
                    it.error?.message?.let { it1 -> showToast(it1) }
                }
            }





        })
        //保存成功之后刷新数据
        EventViewModel.freshLiveData.observeInFragment(this) {
            if(it==1){
                pageNo = 1
                mViewModel.getTodoList(pageNo)
            }
        }
        mViewModel.markHaveDoneLiveData.observe(this,Observer{
              when(!it.isException){
                  true->{
                      if(it.dataBean?.errorCode==0){
                          CommonUtil.showToast("标记已完成Success")
                          EventViewModel.freshLiveData.value=2  //刷新已完成界面
                      }else {
                          it.dataBean?.errorMsg?.let {
                              CommonUtil.showToast(it)
                          }
                      }
                  }
                  false->{
                      it.error?.message?.let { it1 -> CommonUtil.showToast(it1) }
                  }
              }
        })
        mViewModel.deleteLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("删除成功")
                    }else {
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false->{
                    it.error?.message?.let { it1 -> CommonUtil.showToast(it1) }
                }
            }

        })

    }


}
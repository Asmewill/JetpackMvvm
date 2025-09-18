package com.example.oapp.ui.fragment

import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oapp.R
import com.example.oapp.adapter.CollectListAdapter
import com.example.oapp.base.BaseVmDbFragment
import com.example.oapp.bean.CollectArticle
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.FragmentCollectBinding
import com.example.oapp.ui.ContentActivity
import com.example.oapp.utils.CommonUtil
import com.example.oapp.viewmodel.CollectViewModel
import kotlinx.coroutines.*

/**
 * Created by jsxiaoshui on 2021/7/27
 */
class CollectFragment02:BaseVmDbFragment<CollectViewModel,FragmentCollectBinding> (){
    private val list= mutableListOf<CollectArticle.Data>()
    private val collectListAdapter by lazy {
        CollectListAdapter(mViewModel,list)
    }
    private var pageNo: Int=0

    companion object{
        fun getInstance():CollectFragment02{
            val instance=CollectFragment02()
            return instance
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_collect
    }

    override fun createViewModel(): CollectViewModel {
        return ViewModelProvider(this).get(CollectViewModel::class.java)
    }


    override fun initView() {
        mDataBind.recyclerView?.let {
            it.layoutManager=LinearLayoutManager(activity)
            it.adapter=collectListAdapter
        }
        collectListAdapter.setOnItemClickListener {
                baseQuickAdapter, view, i ->
               val list= baseQuickAdapter.data as MutableList<CollectArticle.Data>
               val item=list.get(i)
               val intent=Intent(activity,ContentActivity::class.java)
               intent.putExtra(Constant.CONTENT_TITLE,item.title)
               intent.putExtra(Constant.CONTENT_URL,item.link)
               intent.putExtra(Constant.CONTENT_ID,item.courseId)
               startActivity(intent)
        }
        mDataBind.swipeRefreshLayout.setOnRefreshListener {
            pageNo=0
            mViewModel.getCollectList(pageNo)
        }
        //完整写法
//        GlobalScope.launch (Dispatchers.Main,CoroutineStart.DEFAULT){
//        }
        //省略了CoroutineContext，CoroutineStart二个已经赋值的参数
        GlobalScope.launch {
        }
        //在Activity中使用的时候，可以在onDestory中取消协程
        MainScope().launch {
        }
        //只能在Activity和Fragment中使用，会绑定ViewModel的生命周期
        lifecycleScope.launch {

        }
        //
        //viewModelScope
    }

    override fun initData() {
        mViewModel.getCollectList(pageNo)
    }

    override fun createObserver() {
        mViewModel.collectLiveData.observe(this, Observer {
            mDataBind.swipeRefreshLayout?.isRefreshing=false
            when(!it.isException){
                true->{
                    it.dataBean?.data?.datas?.let {
                        collectListAdapter.setNewData(it.filter { item->
                            if(!TextUtils.isEmpty(item.envelopePic)){
                                item.item_type=2
                            }else{
                                item.item_type=1
                            }
                            true
                        })
                    }
                }
                false->{
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })
        mViewModel.addCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                      CommonUtil.showToast("收藏成功")
                    }else{
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false->{
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })

        mViewModel.cancelCollectLiveData.observe(this, Observer {
            when(!it.isException){
                true->{
                    if(it.dataBean?.errorCode==0){
                        CommonUtil.showToast("已取消收藏")
                    }else{
                        it.dataBean?.errorMsg?.let {
                            CommonUtil.showToast(it)
                        }
                    }
                }
                false->{
                    it.error?.let {
                        CommonUtil.showToast(it.message.toString())
                    }
                }
            }
        })
    }



}
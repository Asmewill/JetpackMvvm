package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSearchBinding
import com.example.wapp.demo.adapter.SearchHistoryAdapter
import com.example.wapp.demo.adapter.SearchHotAdapter
import com.example.wapp.demo.bean.SearchResponse
import com.example.wapp.demo.ext.*
import com.example.wapp.demo.ui.MainActivity
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.viewmodel.SearchViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import okhttp3.Cache
import java.lang.Exception

/**
 * Created by jsxiaoshui on 2021/8/23
 */
class SearchFragment : BaseVmDbFragment<SearchViewModel, FragmentSearchBinding>() {
    private val searchHotAdapter by lazy{
        SearchHotAdapter()
    }
    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter(this)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_search
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initView() {
        setMenu()
        val flexboxLayout = FlexboxLayoutManager(mActivity)
        //主轴为水平方向,起点在左端
        flexboxLayout.flexDirection = FlexDirection.ROW
        //左对齐
        flexboxLayout.justifyContent = JustifyContent.FLEX_START

        search_hotRv.init(
            flexboxLayout, searchHotAdapter, false
        )
        searchHotAdapter.setOnItemClickListener { adapter, view, position ->
            val query:String= (adapter.data[position] as SearchResponse).name
            //try-catch防止短时间内多次快速跳转Fragment出现的bug
            try {
                nav().navigate(R.id.action_searchFragment_to_searchResultFragment, Bundle().apply {
                    this.putString("searchKey", query)
                })
            }catch (e:Exception){

            }
        }
        search_historyRv.init(LinearLayoutManager(mActivity), searchHistoryAdapter, true)
        searchHistoryAdapter.setOnItemClickListener { adapter, view, position ->
            val query:String= adapter.data[position] as String
            //try-catch防止短时间内多次快速跳转Fragment出现的bug
            try {
                nav().navigate(R.id.action_searchFragment_to_searchResultFragment, Bundle().apply {
                    this.putString("searchKey", query)
                })
            }catch (e:Exception){
            }

        }


        search_clear.setOnClickListener {
            MaterialDialog(mActivity).cancelable(false).lifecycleOwner(this).show {
                     this.title(text = "温馨提示")
                    this.message(text = "确定清空吗?")
                    this.negativeButton(text = "取消"){
                       Handler().postDelayed(Runnable {
                            KeyboardUtils.hideSoftInput(mActivity)
                        },300)
                    }
                    this.positiveButton(text = "确定"){
                        mViewModel.historyData.value= arrayListOf()
                        CacheUtil.setSearchHistoryData("")
                        Handler().postDelayed(Runnable {
                            KeyboardUtils.hideSoftInput(mActivity)
                        },300)
                    }
            }
        }
    }

    fun setClearTextVisibility(visibility:Int){
        search_clear.visibility=visibility
    }

    override fun initData() {
        mViewModel.getHotData()
        mViewModel.getHistoryData()

    }

    override fun createObserver() {
        //热门搜索
        mViewModel.searchDataState.observe(mActivity, Observer {
            if (it.isSuccess) {
                searchHotAdapter.setList(it.listData)
            } else {
                ToastUtils.showShort(it.errorMsg)
            }
        })
        //历史记录
        mViewModel.historyData.observe(mActivity, Observer {
            searchHistoryAdapter.setList(it)
            if(it.isNotEmpty()){
                search_clear.visibility=View.VISIBLE
            }else{
                search_clear.visibility=View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView: androidx.appcompat.widget.SearchView =
            menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView
        searchView.run {
            this.maxWidth = Integer.MAX_VALUE
            this.onActionViewExpanded()
            this.queryHint = "输入关键词搜索"
            this.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    updateKey(query)
                    query?.let {
                        nav().navigate(R.id.action_searchFragment_to_searchResultFragment, Bundle().apply {
                            this.putString("searchKey", query)
                        })
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            this.isSubmitButtonEnabled = true
            val field = javaClass.getDeclaredField("mGoButton")
            field.run {
                this.isAccessible = true
                val mGoButton = get(searchView) as ImageView
                mGoButton.setImageResource(R.drawable.ic_search)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun updateKey(keyStr: String) {
        mViewModel.historyData.value?.let {
            if (it.contains(keyStr)) {
                it.remove(keyStr)
            } else if (it.size >= 10) {//当数据超过10个的时候，删除最后一个
                it.removeAt(it.size - 1)
            }
            it.add(0, keyStr)//把数据添加到最前面一行
            CacheUtil.setSearchHistoryData(it.toJson())
            mViewModel.historyData.value = it
        }
    }

    private fun setMenu() {
        setHasOptionsMenu(true)
        toolbar.run {
            mActivity.setSupportActionBar(this)//必须设置，否则不显示toolbar按钮
            this.initClose {
                nav().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.hideSoftInput(mActivity)
    }
}
package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSearchBinding
import com.example.wapp.demo.adapter.SearchHotAdapter
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.initFloatBtn
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.ui.MainActivity
import com.example.wapp.demo.viewmodel.SearchViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * Created by jsxiaoshui on 2021/8/23
 */
class SearchFragment : BaseVmDbFragment<SearchViewModel, FragmentSearchBinding>() {
   val searchHotAdapter by lazy (initializer = {
       SearchHotAdapter()
   })

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
        val flexboxLayout=FlexboxLayoutManager(mActivity)
        //主轴为水平方向,起点在左端
        flexboxLayout.flexDirection=FlexDirection.ROW
        //左对齐
        flexboxLayout.justifyContent=JustifyContent.FLEX_START

        search_hotRv.init(
            flexboxLayout,searchHotAdapter,false
        )

    }

    override fun initData() {
        mViewModel.getHotData()

    }

    override fun createObserver() {
        mViewModel.searchDataState.observe(mActivity, Observer {
            if(it.isSuccess){
                searchHotAdapter.setList(it.listData)
            }else{
               ToastUtils.showShort(it.errorMsg)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView: androidx.appcompat.widget.SearchView = menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView
        searchView.run {
            this.maxWidth = Integer.MAX_VALUE
            this.onActionViewExpanded()
            this.queryHint = "输入关键词搜索"
            this.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        nav().navigate(R.id.action_Main_to_SearchFragment, Bundle().apply {
                            this.putString("searchKey", query)
                        })
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
            this.isSubmitButtonEnabled=true
            val field=javaClass.getDeclaredField("mGoButton")
            field.run {
                this.isAccessible=true
                val mGoButton=get(searchView) as ImageView
                mGoButton.setImageResource(R.drawable.ic_search)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setMenu() {
        setHasOptionsMenu(true)
        toolbar.run {
            mActivity.setSupportActionBar(this)//必须设置，否则不显示toolbar按钮
            this.initClose {
                NavHostFragment.findNavController(this@SearchFragment).navigateUp()
            }
        }
    }
}
package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSearchBinding
import com.example.wapp.demo.viewmodel.SearchViewModel

/**
 * Created by jsxiaoshui on 2021/8/23
 */
class SearchFragment: BaseVmDbFragment<SearchViewModel, FragmentSearchBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
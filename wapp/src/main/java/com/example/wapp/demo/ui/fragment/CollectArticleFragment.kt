package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentCollectArticleBinding
import com.example.wapp.databinding.FragmentCollectBinding
import com.example.wapp.demo.viewmodel.CollectViewModel

/**
 * Created by jsxiaoshui on 2021-11-15
 * 收藏--文章
 */
class CollectArticleFragment: BaseVmDbFragment<CollectViewModel, FragmentCollectArticleBinding>() {
    override fun layoutId(): Int {
       return R.layout.fragment_collect_article
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
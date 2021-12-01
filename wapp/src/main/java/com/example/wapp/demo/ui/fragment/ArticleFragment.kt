package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentArticleBinding
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.navigation.NavHostFragment
import com.example.wapp.demo.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class ArticleFragment:BaseVmDbFragment<ArticleViewModel,FragmentArticleBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_article
    }

    override fun initView() {
        toolbar.initClose(titleStr = "我的文章",onBack={
            NavHostFragment.findNavController(this).navigateUp()
        })

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
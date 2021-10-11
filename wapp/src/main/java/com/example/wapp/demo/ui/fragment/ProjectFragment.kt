package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentProjectBinding
import com.example.wapp.demo.viewmodel.ProjectViewModel

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class ProjectFragment:BaseVmDbFragment<ProjectViewModel,FragmentProjectBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_project
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
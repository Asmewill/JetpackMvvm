package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentToDoBinding
import com.example.wapp.demo.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class ToDoFragment:BaseVmDbFragment<ToDoViewModel,FragmentToDoBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_to_do
    }

    override fun initView() {
        toolbar.title="ToDO"

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}
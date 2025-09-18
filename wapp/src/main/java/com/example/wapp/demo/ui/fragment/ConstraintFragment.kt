package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseViewModel
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentConstraintBinding
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.ext.nav

/**
 * Data :2023/3/14
 * Time:17:57
 * Author:shuij
 *
 */
class ConstraintFragment:BaseVmDbFragment<BaseViewModel,FragmentConstraintBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_constraint
    }
    //https://juejin.cn/post/6949186887609221133#heading-6
    override fun initView() {
        mDataBind.toolbar.initClose ("ConstraintLayout示例"){
           nav().navigateUp()
        }
    }
    override fun initData() {

    }

    override fun createObserver() {

    }

}
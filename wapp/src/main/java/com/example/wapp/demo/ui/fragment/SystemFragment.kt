package com.example.wapp.demo.ui.fragment

import android.os.Bundle
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSquareListBinding
import com.example.wapp.demo.viewmodel.SquareViewModel

/**
 * Created by jsxiaoshui on 2021-11-08
 */
class SystemFragment:BaseVmDbFragment<SquareViewModel,FragmentSquareListBinding>() {
    companion object{
        fun newInstance():SystemFragment{
            val systemFragment=SystemFragment()
            val bundle= Bundle()
            systemFragment.arguments=bundle
            return systemFragment
        }
    }
    override fun layoutId(): Int {
       return  R.layout.fragment_square_list
    }

    override fun initView() {

    }

    override fun initData() {

    }


    override fun createObserver() {

    }
}
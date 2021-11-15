package com.example.oapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by jsxiaoshui on 2021/7/22
 */
abstract  class BaseVmDbFragment<VM:BaseViewModel,DB:ViewDataBinding>:BaseVmFragment<VM>() {
    lateinit var mDataBind: DB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBind=DataBindingUtil.inflate(inflater,layoutId(),container,false)
        mDataBind.lifecycleOwner=this
        return mDataBind.root
    }

    override fun lazyLoad() {

    }

}
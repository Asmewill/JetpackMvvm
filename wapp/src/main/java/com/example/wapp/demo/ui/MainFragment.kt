package com.example.wapp.demo.ui

import androidx.lifecycle.ViewModelProvider
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentMainBinding
import com.example.wapp.demo.ext.init
import com.example.wapp.demo.ext.initMain
import com.example.wapp.demo.permission.PermissionsManager
import com.example.wapp.demo.permission.PermissionsResultAction
import com.example.wapp.demo.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by jsxiaoshui on 2021/8/18
 */
class MainFragment :BaseVmDbFragment<MainViewModel,FragmentMainBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun createViewModel(): MainViewModel {
        return ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun initView() {
       mainViewpager.initMain(this)
       mainBottom.init {
            when(it){
                R.id.menu_main->{
                    mainViewpager.setCurrentItem(0,false)
                }
                R.id.menu_project-> {
                    mainViewpager.setCurrentItem(1,false)
                }
                R.id.menu_system->{
                    mainViewpager.setCurrentItem(2,false)
                }
                R.id.menu_public->{
                    mainViewpager.setCurrentItem(3,false)
                }
                R.id.menu_me-> {
                    mainViewpager.setCurrentItem(4,false)
                }
            }
       }
    }

    override fun initData() {
        requestPermissions()
    }

    override fun createObserver() {

    }
    // TODO: 2019/12/19 0019 有必要修改一下
    private fun requestPermissions() {
        PermissionsManager.getInstance()
            .requestAllManifestPermissionsIfNecessary(mActivity, object : PermissionsResultAction() {
                override fun onGranted() {

                }
                override fun onDenied(permission: String?) {

                }
            })
    }
}
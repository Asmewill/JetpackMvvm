package com.example.wapp.demo.ui.fragment

import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentDemoBinding
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.navigation.NavHostFragment
import com.example.wapp.demo.room.AppDataBase
import com.example.wapp.demo.room.User
import com.example.wapp.demo.viewmodel.DemoViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Data :2023/3/13
 * Time:10:30
 * Author:shuij
 *
 */
class DemoFragment:BaseVmDbFragment<DemoViewModel,FragmentDemoBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_demo
    }

    override fun initView() {
       toolbar.initClose(titleStr = "示例"){
           NavHostFragment.findNavController(this).navigateUp()
       }

    }
    override fun initData() {
        mDataBind.click=ProxyClick()

    }

    override fun createObserver() {

    }
    inner class  ProxyClick{
        var i:Int=-1
        fun roomDataBase(){
            NavHostFragment.findNavController(this@DemoFragment).navigate(R.id.action_DemoFragment_to_RoomFragment)
        }
        fun toConstraintLayout(){
            NavHostFragment.findNavController(this@DemoFragment).navigate(R.id.action_DemoFragment_to_ConstraintFragment)
        }
    }
}
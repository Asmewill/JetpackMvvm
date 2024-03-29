package com.example.oapp.ui

import android.content.res.ColorStateList
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.adapter.ToDoPageAdapter
import com.example.oapp.base.BaseViewModel
import com.example.oapp.base.BaseVmDbActivity
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.ActivityTodoBinding
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Created by jsxiaoshui on 2021/7/27
 */
@Route(path = Constant.PagePath.TODO)
class ToDoActivity: BaseVmDbActivity<BaseViewModel, ActivityTodoBinding>() {
    private val toDoPagerAdapter by lazy {
        ToDoPageAdapter(supportFragmentManager)
    }

    override fun createViewModel(): BaseViewModel {
         return ViewModelProvider(this).get(BaseViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.activity_todo
    }

    override fun initView() {
        toolbar?.title="ToDo"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewPager?.adapter=toDoPagerAdapter
        floating_action_btn.setOnClickListener {
            ARouter.getInstance().build(Constant.PagePath.COMMON)
                .withString(Constant.PAGE_TYPE,Constant.Type.ADD_TODO_TYPE_KEY)
                .navigation()
        }
        bottom_navigation?.let {
            it.setOnNavigationItemSelectedListener {menuItem->
                when(menuItem.itemId){
                    R.id.action_notodo->{
                        viewPager.currentItem = 0
                    }
                    R.id.action_completed->{
                        viewPager.currentItem = 1
                    }
                }
                return@setOnNavigationItemSelectedListener true
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun initData() {


    }
    override fun createObserver() {

    }

    override fun initColor() {
        super.initColor()
        floating_action_btn.backgroundTintList=ColorStateList.valueOf(mThemeColor)
    }
}
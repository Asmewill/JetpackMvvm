package com.example.oapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.oapp.bean.WeChatData
import com.example.oapp.ui.fragment.WeChatItemFragment

/**
 * Created by jsxiaoshui on 2021/6/30
 */
class WeChatPageAdapter(private val listData:ArrayList<WeChatData>,fm:FragmentManager):FragmentStatePagerAdapter(fm) {
    private  val  fragmentList=ArrayList<Fragment>()
    init {
       listData.forEach {
           fragmentList.add(WeChatItemFragment.getInstance(it.id))
       }
    }
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(p0: Int): Fragment {
        return fragmentList.get(p0)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listData.get(position).name
    }
}
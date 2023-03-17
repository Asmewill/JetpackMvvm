package com.example.oapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.oapp.bean.KnowledgeData
import com.example.oapp.bean.WeChatData
import com.example.oapp.ui.fragment.KnowledgeItemFragment
import com.example.oapp.ui.fragment.WeChatItemFragment

/**
 * Created by jsxiaoshui on 2021/6/30
 */
class KnowledgePageAdapter(private val listData:MutableList<KnowledgeData.ChildrenBean>, fm:FragmentManager):FragmentStatePagerAdapter(fm) {
    private  val  fragmentList= mutableListOf<Fragment>()
    init {
       listData.forEach {
           fragmentList.add(KnowledgeItemFragment.getInstance(it.id))
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
package com.example.oapp.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.oapp.ui.fragment.DoneFragment
import com.example.oapp.ui.fragment.ToDoFragment

/**
 * Created by jsxiaoshui on 2021/7/27
 */
@SuppressLint("WrongConstant")
class ToDoPageAdapter(fm:FragmentManager):FragmentStatePagerAdapter(fm,0) {
    var listFragment= arrayListOf<Fragment>()
    init {
        listFragment.add(ToDoFragment())
        listFragment.add(DoneFragment())
    }
    override fun getCount(): Int {
       return listFragment.size
    }

    override fun getItem(p0: Int): Fragment {
        return listFragment.get(p0)
    }

}
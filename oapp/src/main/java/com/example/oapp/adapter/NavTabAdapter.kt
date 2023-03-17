package com.example.oapp.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.oapp.R
import com.example.oapp.bean.NavBean
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView

/**
 * Created by jsxiaoshui on 2021/7/6
 */
class NavTabAdapter(private val context:Context,private val list:ArrayList<NavBean>):TabAdapter {
    override fun getCount(): Int {
       return list.size
    }

    override fun getBadge(position: Int): ITabView.TabBadge? {
           return null
    }

    override fun getIcon(position: Int): ITabView.TabIcon? {
        return null
    }

    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder().setContent(list.get(position).name).setTextColor(ContextCompat.getColor(context,
            R.color.colorAccent),ContextCompat.getColor(context,
            R.color.Black)).build()
    }

    override fun getBackground(position: Int): Int {
         return ContextCompat.getColor(context,
             R.color.colorAccent)
    }
}
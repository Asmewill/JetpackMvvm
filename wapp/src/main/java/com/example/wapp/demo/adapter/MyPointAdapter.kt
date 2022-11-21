package com.example.wapp.demo.adapter


import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.Point
import com.example.wapp.demo.ui.fragment.MyPointFragment


/**
 * Created by jsxiaoshui on 2021-10-25
 */
class MyPointAdapter(private val fragment:MyPointFragment):BaseQuickAdapter<Point, BaseViewHolder>(R.layout.item_mypoint) {

    override fun convert(holder: BaseViewHolder, item:Point) {
        var tv_rank:TextView=holder.getView(R.id.tv_rank)
        var tv_name:TextView=holder.getView(R.id.tv_name)
        var tv_count:TextView=holder.getView(R.id.tv_count)
        tv_rank.text=item.rank
        tv_name.text=item.username
        tv_count.text=item.coinCount.toString()
    }
}
package com.example.wapp.demo.adapter


import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.Point
import com.example.wapp.demo.bean.PointRecordBean
import com.example.wapp.demo.ui.fragment.MyPointFragment
import com.example.wapp.demo.ui.fragment.PointRecordFragment
import com.example.wapp.demo.utils.DatetimeUtil


/**
 * Created by jsxiaoshui on 2021-10-25
 */
class PointRecordAdapter(private val fragment:PointRecordFragment):BaseQuickAdapter<PointRecordBean, BaseViewHolder>(R.layout.item_point_record) {

    override fun convert(holder: BaseViewHolder, item:PointRecordBean) {
        var item_integralhistory_title:TextView=holder.getView(R.id.item_integralhistory_title)
        var item_integralhistory_date:TextView=holder.getView(R.id.item_integralhistory_date)
        var item_integralhistory_count:TextView=holder.getView(R.id.item_integralhistory_count)
        item_integralhistory_title.text=item.desc
        item_integralhistory_date.text=DatetimeUtil.formatDate(item.date,DatetimeUtil.DATE_PATTERN_SS)
        item_integralhistory_count.text=item.coinCount.toString()
    }
}
package com.example.oapp.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.PointBean
import org.w3c.dom.Text

/**
 * Created by jsxiaoshui on 2021/7/23
 */
class RankListAdapter:BaseQuickAdapter<PointBean.Data,BaseViewHolder>(R.layout.item_rank_list){
    override fun convert(holder: BaseViewHolder?, item: PointBean.Data?) {
        holder?:return
        item?:return
        val tv_ranking=holder.getView<TextView>(R.id.tv_ranking)
        val tv_username=holder.getView<TextView>(R.id.tv_username)
        val tv_score=holder.getView<TextView>(R.id.tv_score)
        tv_ranking.text=item.rank
        tv_username.text=item.username
        tv_score.text=item.coinCount.toString()


    }
}
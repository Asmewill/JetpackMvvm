package com.example.oapp.adapter


import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.ScoreBean

/**
 * Created by jsxiaoshui on 2021/7/26
 */
class ScoreAdapter:BaseQuickAdapter<ScoreBean.Data,BaseViewHolder>(R.layout.item_socre_list){
    override fun convert(holder: BaseViewHolder?, item: ScoreBean.Data?) {
        holder?:return
        item?:return
        val tv_reason=holder.getView<TextView>(R.id.tv_reason)
        val tv_desc=holder.getView<TextView>(R.id.tv_desc)
        val tv_score=holder.getView<TextView>(R.id.tv_score)
        item.reason?.apply {
            tv_reason.text=this
        }
        item.desc?.apply {
            tv_desc.text=this
        }
        item.coinCount?.apply {
            tv_score.text=this.toString()
        }

    }
}
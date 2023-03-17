package com.example.oapp.adapter

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.R
import com.example.oapp.bean.NavBean
import com.example.oapp.constant.Constant
import com.example.oapp.ext.showToast
import com.example.oapp.ui.ContentActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import org.jetbrains.anko.layoutInflater
import q.rorbin.verticaltablayout.util.DisplayUtil

/**
 * Created by jsxiaoshui on 2021/7/6
 */
class NavRightAdapter:BaseQuickAdapter<NavBean,BaseViewHolder>(R.layout.item_navigation_list) {
    override fun convert(holder: BaseViewHolder?, item: NavBean?) {
        holder?:return
        item?:return
        val item_navigation_tv=holder.getView<TextView>(R.id.item_navigation_tv)
        val item_navigation_flow_layout=holder.getView<TagFlowLayout>(R.id.item_navigation_flow_layout)
        item.name?.let {
            item_navigation_tv.setText(it)
        }
        item_navigation_flow_layout.adapter =object:TagAdapter<NavBean.ArticlesBean>(item.articles){
            override fun getView(p0: FlowLayout?, p1: Int, p2: NavBean.ArticlesBean?): View {
                val flow_layout_tv:TextView= mContext.layoutInflater.inflate(R.layout.flow_layout_tv,item_navigation_flow_layout,false) as TextView
                flow_layout_tv.setText(p2!!.title)
                val padding=DisplayUtil.dp2px(mContext,10f);
                flow_layout_tv.setPadding(padding,padding,padding,padding)
                flow_layout_tv.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                return  flow_layout_tv
            }
        }
        item_navigation_flow_layout.setOnTagClickListener { view, i, flowLayout ->
            ARouter.getInstance().build(Constant.PagePath.CONTENT)
                .withInt(Constant.CONTENT_ID, item.articles?.get(i)?.id?:0)
                .withString(Constant.CONTENT_URL, item.articles?.get(i)?.link)
                .withString(Constant.CONTENT_TITLE, item.articles?.get(i)?.title)
                .navigation()
            return@setOnTagClickListener true
        }
    }
}
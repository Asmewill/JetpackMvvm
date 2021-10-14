package com.example.wapp.demo.adapter

import android.view.View
import com.example.wapp.R
import com.example.wapp.demo.bean.BannerResponse
import com.zhpan.bannerview.BaseBannerAdapter

/**
 * Created by jsxiaoshui on 2021-10-14
 */
class HomeBannerAdapter : BaseBannerAdapter<BannerResponse, HomeBannerViewHolder>() {
    override fun createViewHolder(itemView: View, viewType: Int): HomeBannerViewHolder {
          return HomeBannerViewHolder(itemView)
    }
    override fun onBind(
        holder: HomeBannerViewHolder?,
        data: BannerResponse?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data,position,pageSize)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemhome
    }
}
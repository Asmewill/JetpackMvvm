package com.example.wapp.demo.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.wapp.R
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.bean.BannerResponse
import com.zhpan.bannerview.BaseViewHolder

/**
 * Created by jsxiaoshui on 2021-10-14
 */
class HomeBannerViewHolder(view: View):BaseViewHolder<BannerResponse>(view) {
    override fun bindData(data: BannerResponse?, position: Int, pageSize: Int) {
        val img=itemView.findViewById<ImageView>(R.id.bannerhome_img)
        data?.let {
           Glide.with(img.context).load(it.imagePath).transition(
               DrawableTransitionOptions.withCrossFade(500)
           ).into(img)
        }
    }
}
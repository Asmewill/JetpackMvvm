package com.example.wapp.demo.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.wapp.R
import com.example.wapp.demo.bean.SplashBean
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


/**
 * Created by jsxiaoshui on 2021/8/18
 */
class SplashBannerAdapter: BaseBannerAdapter<SplashBean, SplashBannerAdapter.BannerViewHolder>() {

    override fun createViewHolder(itemView: View, viewType: Int): BannerViewHolder {
        return BannerViewHolder(itemView)
    }

    override fun onBind(holder: BannerViewHolder?, data: SplashBean?, position: Int, pageSize: Int) {
         holder?.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int {
       return R.layout.item_banner
    }

    inner class BannerViewHolder(itemView:View) :BaseViewHolder<SplashBean>(itemView){
        override fun bindData(data: SplashBean?, position: Int, pageSize: Int) {
            val textView = findView<TextView>(R.id.banner_text)
            val image=findView<ImageView>(R.id.image)
            textView.text = data?.mes
            image.setBackgroundResource(data?.drawableId!!)
        }
    }
}



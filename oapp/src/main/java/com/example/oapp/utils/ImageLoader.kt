package com.example.oapp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by jsxiaoshui on 2021/6/29
 */
object ImageLoader {
    fun loadIv(context:Context,url:String,iv:ImageView){
        Glide.with(context).load(url).into(iv)
    }
}
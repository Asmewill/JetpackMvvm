package com.example.oapp.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.MyApp
import com.example.oapp.R
import com.example.oapp.utils.SettingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.schedulers.Schedulers


/**
 * Created by jsxiaoshui on 2021/6/25
 */
fun Activity.showToast(str:String){
    Toast.makeText(applicationContext,str,Toast.LENGTH_LONG).show();
}

fun Fragment.showToast(str: String){
    Toast.makeText(MyApp.context,str,Toast.LENGTH_LONG).show();
}
fun <T> Observable<T>.applySchdules():Observable<T>{
    return subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
fun <T,k : BaseViewHolder?>BaseQuickAdapter<T,k>.showToast(str:String){
    Toast.makeText(MyApp.instance,str,Toast.LENGTH_LONG).show();
}

@SuppressLint("WrongConstant")
fun Activity.showSnackMsg(msg:String){
//    val snackbar= Snackbar.make(this.window.decorView,msg, Snackbar.LENGTH_SHORT)
//    val view=snackbar.view
//    view.findViewById<TextView>(R.id.snackbar_text).setTextColor(ContextCompat.getColor(this,R.color.white))
//    snackbar.show()
    Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
}
fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    //floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(appContext)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}

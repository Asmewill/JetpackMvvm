package com.example.oapp.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.oapp.MyApp
import com.example.oapp.R
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
    val snackbar= Snackbar.make(this.window.decorView,msg, Snackbar.LENGTH_SHORT)
    val view=snackbar.view
    view.findViewById<TextView>(R.id.snackbar_text).setTextColor(ContextCompat.getColor(this,R.color.white))
    snackbar.show()
}


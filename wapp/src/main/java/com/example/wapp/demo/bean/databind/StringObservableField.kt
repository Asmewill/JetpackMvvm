package com.example.wapp.demo.bean.databind

import androidx.databinding.ObservableField

/**
 * Data :2023/2/13
 * Time:16:41
 * Author:shuij
 *
 */
open class  StringObservableField(value:String=""):ObservableField<String>(value) {

    override fun get(): String {
        return super.get()!!
    }
}
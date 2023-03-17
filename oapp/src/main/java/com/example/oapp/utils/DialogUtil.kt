package com.example.oapp.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils

/**
 * Created by Owen on 2019/11/1
 */
object DialogUtil {
    /**
     * 获取一个Dialog
     *
     * @param context
     * @return
     */
    fun getDialog(context: Context): androidx.appcompat.app.AlertDialog.Builder {
        return androidx.appcompat.app.AlertDialog.Builder(context)
    }

    fun getConfimDialog(context:Context,message:String,onClickListener: DialogInterface.OnClickListener): androidx.appcompat.app.AlertDialog.Builder {
        val builder= getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton("确定",onClickListener)
        builder.setNegativeButton("取消",null)
        return builder

    }


    fun getWaitDialog(context:Context,message:String):ProgressDialog{
         val waitDialog=ProgressDialog(context)
         if(!TextUtils.isEmpty(message)){
             waitDialog.setMessage(message)
         }
        return waitDialog
    }


}
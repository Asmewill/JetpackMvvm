package com.example.oapp.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.oapp.R
import com.example.oapp.base.BaseActivity
import com.example.oapp.constant.Constant
import kotlinx.android.synthetic.main.activity_error.*
import kotlinx.android.synthetic.main.activity_knowledge.*

/**
 * Created by jsxiaoshui on 2021/8/5
 */
@Route(path = Constant.PagePath.ERROR)
class ErrorActivity : BaseActivity() {
    override fun attachLayoutRes(): Int {
        return R.layout.activity_error
    }

    override fun initView() {
        toolbar?.title = "发生错误"
        setSupportActionBar(toolbar)
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        //从新启动
        errorRestart.setOnClickListener {
            CustomActivityOnCrash.restartApplication(this@ErrorActivity, config!!)
        }
        errorSendError.setOnClickListener {
            //We retrieve all the error data and show it
            val dialog = AlertDialog.Builder(this@ErrorActivity)
                .setTitle(R.string.customactivityoncrash_error_activity_error_details_title)
                .setMessage(
                    CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                        this@ErrorActivity,
                        intent
                    )
                )
                .setPositiveButton(
                    R.string.customactivityoncrash_error_activity_error_details_close,
                    null
                )
                .setNeutralButton(
                    R.string.customactivityoncrash_error_activity_error_details_copy
                ) { dialog, which ->
                    copyErrorToClipboard()
                }
                .show()
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size)
            )

        }
    }

    @SuppressLint("NewApi")
    private fun copyErrorToClipboard() {
        val errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(
            this@ErrorActivity,
            intent
        )
//        var   clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//
//        //Are there any devices without clipboard...?
//        if (clipboard != null) {
//            var clip = ClipData.newPlainText(
//                getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label),
//                errorInformation
//            )
//            clipboard.primaryClip = clip
//            Toast.makeText(
//                this@ErrorActivity,
//                R.string.customactivityoncrash_error_activity_error_details_copied,
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }

    override fun initData() {


    }
}
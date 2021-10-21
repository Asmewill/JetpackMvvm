package me.hgj.jetpackmvvm.demo.app.weight.loadCallBack

import android.content.Context
import android.view.View
import com.example.wapp.R
import com.kingja.loadsir.callback.Callback


class LoadingCallback2 : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_loading_2
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}
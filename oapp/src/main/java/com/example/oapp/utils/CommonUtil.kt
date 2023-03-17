package com.example.oapp.utils

import android.graphics.Color
import android.os.SystemClock
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.oapp.MyApp
import java.util.*

/**
 * Created by Owen on 2019/10/31
 */
object CommonUtil {

    /**
     * 获取随机rgb颜色值
     */
    fun randomColor(): Int {
        val random = Random()
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        var red = random.nextInt(190)
        var green = random.nextInt(190)
        var blue = random.nextInt(190)
//        if (SettingUtil.getIsNightMode()) {
//            //150-255
//            red = random.nextInt(105) + 150
//            green = random.nextInt(105) + 150
//            blue = random.nextInt(105) + 150
//        }
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }

    fun forceStopRecycleViewScroll(recycleView:RecyclerView){
        recycleView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),MotionEvent.ACTION_CANCEL,0f,0f,0))
    }
    fun showToast(msg:String){
        Toast.makeText(MyApp.context,msg,Toast.LENGTH_LONG).show()
    }

}
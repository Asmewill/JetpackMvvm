package studynote.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File

/**
 * Created by jsxiaoshui on 2021/8/8
 */

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
fun main() {
    //普通匿名函数
    //()->Unit
    //匿名函数内部this指向一个File对象，隐式调用
    //File.()->Unit



    //普通扩展函数
    fun String.addExt()="!".repeat(this.count())
    //泛型扩展函数
    fun <T> T.easyPrint():Unit = println(this)
    //block是泛型扩展匿名函数
//     fun <T> T.apply(block: T.() -> Unit): T {
//          block()
//        return this
//    }

    //apply做到了接收者对象隐式调用
    val file=File("xxx").apply {
        setReadable(false)
    }
}

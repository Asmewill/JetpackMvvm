package studynote

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by jsxiaoshui on 2021/8/9
 */
fun main() {

    println("---------------------函数类型作为返回值类型-------------------")
    val getDiscountWords= configDiscountWords()
    println(getDiscountWords)//Function1<java.lang.String, java.lang.String>
    println(getDiscountWords("toothbrush"))//2027 年，双11 toothbrush 促销倒计时:43 小时
    showOnBoard("舒适达牙膏", configDiscountWords())
    println("---------------------函数引用-------------------")
    showConfig(::config)

}
private fun config():String{
    return "print config info .."
}

private  fun showConfig(block:()->String){
    val result=block.invoke() //一定要加invoke触发该函数的函数体
    println(result)
}


//函数类型作为返回值类型
fun configDiscountWords():(String)->String{
    return {  goodsName->
        val currentYear="2027"
        var  hour=(1..24).shuffled().first()
        hour+=20
        "$currentYear 年，双11 $goodsName 促销倒计时:$hour 小时"
    }

}

fun showOnBoard(goodsName:String,block:(String)->String){
    println(block(goodsName))

}
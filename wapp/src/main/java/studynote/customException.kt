package studynote

import java.lang.Exception
import java.lang.IllegalArgumentException

/**
 * Created by jsxiaoshui on 2021/8/9
 */
fun main() {
    val  str=""
    val aa=str?:"buttery" //?:  空合并操作符

    var number:Int?=null
    try {
      checkOperation(number)
    }catch (e:Exception){
        println(e)
    }
}
fun checkOperation(number:Int?){
    number?:throw CustomException()
}
//自定义异常
class CustomException :IllegalArgumentException("骚操作导致如此，活该")
package studynote

import studynote.reified.Boy
import studynote.reified.Girl
import studynote.reified.Human

/**
 * Created by jsxiaoshui on 2021/8/9
 */


class MagicBox<T:Human>(){
    //reified帮你检查泛型的参数类型，Kotlin不允许对泛型参数T做类型检查，因为泛型参数类型会被类型擦除,也就是说
    //T的类型信息在运行时时不可知的，Java也有这样的规则
    inline fun <reified T> randomOfBackUp(backUp:()->T):T{
        val items= listOf(Boy("jack",20),Girl("yingzi",28))
        val random=items.shuffled().first()
        return if(random is T){
             random
        }else{
            backUp()
        }
    }
}

fun main() {
   val magicBox=MagicBox<Boy>()
   val result=magicBox.randomOfBackUp {
        Girl("YuanFang",27)
   }
   println(result.toString())
    val array= arrayOf(1,2,3,4)
    println("array取值的二种方式---->array.get(0):${array.get(0)},array[0]:${array[0]}")

}
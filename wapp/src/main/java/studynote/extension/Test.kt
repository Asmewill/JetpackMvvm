package studynote.extension
import  studynote.extension.randomTake as randomTakeAlis
/**
 * Created by jsxiaoshui on 2021/8/8
 */
fun main() {

    val list= listOf<String>("jack","jack1","jack3")
    val set= setOf<String>("Tom1","Tom2","Tom3")
   // list.randomTake()
    list.randomTakeAlis()//重命名扩展
}
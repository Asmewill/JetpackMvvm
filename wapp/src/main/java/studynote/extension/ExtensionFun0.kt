package studynote.extension

/**
 * Created by jsxiaoshui on 2021/8/8
 */
//泛型扩展函数
fun <T> T.easyPrint():T {
    println(this)
    return this
}
fun String.addExt(amount:Int=1)=this+"!".repeat(amount)

fun main() {
    "abc".easyPrint().addExt(2).easyPrint()
}
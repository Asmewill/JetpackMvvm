package studynote

/**
 * Created by jsxiaoshui on 2021/8/9
 */
fun main() {
    //infix关键字适用于有单个参数的扩展函数和有单个参数的类函数,
    //可以让你以更简洁的语法调用函数，如果一个函数定义了使用了infix关键字
    //那么调用它时，接收者和函数之间的点操作以及参数的一对括号都可以不要

    //until使用了infix关键字
    val itemsOrigin:IntRange= (1.until(10) )
    val items:IntRange= (1 until 10)

}
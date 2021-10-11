package studynote

/**
 * Created by jsxiaoshui on 2021/8/9
 */
data class componentTest( val experience:Int,val level:Int) {
    // operator fun component1()=experience
     //operator fun component2()=level
}

fun main() {
     //解构申明的后台实现就是声明component1,component2等若干个组件函数，
     // 让每个函数负责管理你想返回的一个属性数据
     //如果你定义一个数据类，它会自动为所有定义在主构造函数的属性添加对应的组件函数
    val (x,y)=componentTest(10,4)
    println("x:$x ,y:$y")
}
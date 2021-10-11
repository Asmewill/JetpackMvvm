package studynote

import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Created by jsxiaoshui on 2021/8/9
 */
@RequiresApi(Build.VERSION_CODES.N)
fun main() {
    println("-------------不可变List------------------")
    val list= listOf("jason","jack","jaron")
    val (x,y,z)=list
    println("$x,$y,$z")

    println("-------------可变List------------------")
    val mutableList= mutableListOf("Jack","Rose","Owen")
    mutableList.add("xiaofeng")
    println(mutableList.toString())

    println("-------------不可变map------------------")
    val map= mapOf("Jack" to 20,"Rose" to 23,"Owen" to 30)
    println("-------------可变Map------------------")
    //可变Map
    val mutableMap= mutableMapOf("Jack" to 20,"Rose" to 23,"Owen" to 30)
    mutableMap+="jimmy" to 28
    mutableMap.put("jimmy",30)
    mutableMap.getOrDefault("Rose",18)
    println(mutableMap)
    println("-------------遍历Map style1------------------")
    //遍历Map 方式一
    mutableMap.forEach { key, value ->
       println("$key,$value")
    }
    println("-------------遍历Map style2------------------")
    //遍历Map 方式二
    mutableMap.forEach {
        println("${it.key},${it.value}")
    }

    println("-------------不可变Set------------------")
    val set= setOf("jason","jack","jaron")
    println(set.toString())
    println("-------------可变Set------------------")
    val mutableSet= mutableSetOf("jason","jack","jaron")
    mutableSet.add("xiaofeng")
    mutableSet.add("jack")
    println(mutableSet.toString())


}
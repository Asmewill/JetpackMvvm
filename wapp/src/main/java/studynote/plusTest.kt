package studynote

/**
 * Created by jsxiaoshui on 2021/8/9
 */
data class Conridnate(var x:Int,var y:Int){
    //+   plus
    //+=  plusAssign
    //==  equals
    //>   compareTo
    //[]     get
    //..  rangTo
    //in  contains

    val isInBounds=x>0&&y>0
    //+运算符重载
    operator fun plus(other:Conridnate)=Conridnate(x+other.x,y+other.y)

}

fun main() {
    val c1=Conridnate(10,20)
    val c2=Conridnate(10,20)
    println(c1+c2)

}
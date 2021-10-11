package studynote

/**
 * Created by jsxiaoshui on 2021/8/9
 */
fun main() {
    //先决条件函数，你可以用它定义先决条件，
    // 条件必须满足，目标代码才能执行,否则就会抛出异常
    val name:String?="null"
    checkNotNull(name)//如果参数为null,就会抛出java.lang.IllegalStateException
    requireNotNull(null)//如果参数为null,java.lang.IllegalArgumentException

    //开启kotlin断言开关  run -> Edit configuration -> VM options, 在文本框中输入“-ea”， 点击OK即可。
    // assert(false) //如果参数为false,就抛出java.lang.AssertionError
   // require(false) //如果参数为false,就抛出java.lang.IllegalArgumentException

    val name1:Any="aaa"
    //error(name1) //抛出java.lang.IllegalStateException
}
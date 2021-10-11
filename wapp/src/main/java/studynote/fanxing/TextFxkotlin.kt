package studynote.fanxing

import studynote.fanxing.obj.Person
import studynote.fanxing.obj.Student

/**
 * Created by jsxiaoshui on 2021/7/31
 */
class TextFxkotlin {
    //extends out
    var list:MutableList<out Person> = mutableListOf()//不能修改，可以获取 ? extends
    var list1:MutableList<in Student> = mutableListOf()//能修改，不能获取 ? super
    fun testOutIn(){
        //list.add(1,Student())
      //  list1.add(1,Student())
    }

}
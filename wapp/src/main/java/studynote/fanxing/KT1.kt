package studynote.fanxing

/**
 * Created by jsxiaoshui on 2021/7/31
 * kotlin 声明权限的控制，只能获取，不能修改
 */

class KT1 <out T> {
    fun getData():T?=null

//    fun setData(d :T){
//
//    }
//    fun addData(d:T){
//
//    }
}
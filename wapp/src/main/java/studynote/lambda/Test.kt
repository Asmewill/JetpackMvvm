package studynote

/**
 * Created by jsxiaoshui on 2021/7/31
 */
//函数是一等公民
fun main(args:Array<String>){

    val method:(String ,String)->Unit= { astr,bstr->
        println("astr:$astr,bstr:$bstr")
    }
    method("shuijian","男")
    //println(method)

    val method02:()->Unit={
        println("shuijian")
    }
    method02.invoke()
    val method03:(String)->Unit = {
        println(it)
    }
    method03("shuijian")
    var method04:(Int)->Unit={
        when(it){
            1->{
                println(1)
            }
            in 20 .. 30->{
                println(20 .. 30)
            }
            else->{
                println("都不满足")
            }
        }
    }
    var method05:(Int,Int)->Unit={aNumber,_->
        println("$aNumber")
    }

    var method06:(String)->String={str->str }
    //Dart中匿名函数的写法
//    var printNum=(){
//        println(123);
//    }
//    printNum();

    var method7:()->Unit={
        println("")
    }


}

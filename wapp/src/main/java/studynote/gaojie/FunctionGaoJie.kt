package com.example.wapp

/**
 * Created by jsxiaoshui on 2021/8/8
 */
fun main() {
    println("---函数式编程的设计理念就是不可变数据的副本在链上的函数间传递---")


    println("----------------------map高阶---------------------")
    //Map变换函数会遍历接收者集合，让变换器函数作用于集合里的各个元素，
    //返回结果是包含已经修改的元素的集合,会作为链上,下一个函数的输入
    val animals= listOf<String>("cat1","cat2","cat3","cat4")
    val babies=animals.map {
        "A baby $it"
    }.map {
        "$it ,with the beautiful little tail!"
    }
    println(animals)
    println(babies)
    val animalsLength=animals.map { it.length }
    println(animalsLength)


    println("----------------------flatmap高阶---------------------")
    //flatMap函数操作一个集合的集合,将其中多个集合中的元素合并后返回一个包含所有元素的单一集合
    val items= listOf(
        listOf("red apple","green apple","blue apple"),
        listOf("red fish","blue fish"),
        listOf("yellow banala","green banala")
    )
    val flatItems=items.flatMap {
        it
    }
    println("flatItems:$flatItems")
    val redItems=items.flatMap {
        it.filter { item->
            item.contains("red")
        }
    }
    println("redItems:$redItems")

    println("----------------------filter高阶---------------------")
    //filter高阶函数，接收一个predicate函数，如果返回true，就把受检元素返回到新的集合，
    // 如果返回false,那么就把受检元素移出新集合。
    val result= listOf("Jack","Jimmy","Rose","Tom").filter {
        it.contains("J")
    }
    println("result:$result")

    println("----------------------filter map高阶 求素数---------------------")
    //素数：除了能被1和他自身，不能被任何数整除
    val numbers= listOf(7,4,8,4,3,22,18,11)
    val shushu=numbers.filter { number->
        (2 until number).map {
            number % it
        }.none {
            it == 0
        }
    }
    println("shushu:$shushu")


    println("----------------------zip高阶---------------------")
    //zip高阶函数，用来合并二个集合，返回一个包含键值对的新集合
    val names= listOf("jack","jason","Tommy")
    val ages= listOf(18,20,30)
    val map=names.zip(ages)
    println(map)


    println("----------------------fold高阶---------------------")
    //flod高阶函数，接受一个初始累加器值，随后会根据匿名函数的结果更新
    val folderValue = listOf(1, 2, 3, 4).fold(
        0,
        { accumulator, number ->
            println("Accumulated value :$accumulator")
            accumulator+(number*3)
        }
    )
    println("folderValue:$folderValue")








}
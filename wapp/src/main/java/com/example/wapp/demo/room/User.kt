package com.example.wapp.demo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data :2023/3/13
 * Time:16:42
 * Author:shuij
 *如果需要在表里新增一个字段如test字段，
 * 只需要增加一个@ColumnInfo注解，并为其赋一个默认值
 */
@Entity(tableName = "t_user")
data class User(
    @PrimaryKey(autoGenerate = true)  var id:Long,
    @ColumnInfo(name="user_name") var name :String,
    @ColumnInfo(name="user_age") var age:Int,
    //如果需要在表里新增一个字段如test字段，只需要增加一个@ColumnInfo注解，并为其赋一个默认值
    @ColumnInfo(name="user_sex", defaultValue = "") var sex:String,

    @ColumnInfo(name="user_xueli", defaultValue = "") var xueli:String
) {
    constructor():this(0,"",0,"","") //构造方法一
    constructor(name:String ,age:Int,sex:String,xueli:String):this(0,name,age,sex,xueli) //构造方法二
}
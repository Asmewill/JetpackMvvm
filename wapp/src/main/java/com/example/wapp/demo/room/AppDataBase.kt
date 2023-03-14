package com.example.wapp.demo.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.wapp.demo.MyApp

/**
 * Data :2023/3/13
 * Time:17:23
 * Author:shuij
 *
 */
@Database(entities =[User::class], version = 3, exportSchema = true, autoMigrations = [
    AutoMigration(from = 1,to=2),AutoMigration(from=2,to=3)
])
abstract class AppDataBase:RoomDatabase() {

    abstract fun userDao():UserDao
    /***
     * 单例模式
     */
    companion object {
        private var instance:AppDataBase?=null
        fun getInstance():AppDataBase{
            if(instance==null){
                instance= Room.databaseBuilder(MyApp.instance,AppDataBase::class.java,"user.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}
package com.example.wapp.demo.room

import androidx.room.*

/**
  * Data :2023/3/13
   * Time:16:59 
   * Author:shuij
   *  
  */
@Dao
interface UserDao {
    //插入单个用户
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user:User)
    //插入多个用户
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(users:List<User>)
    //删除用户
    @Delete
    fun deleteUser(user:User)
    @Delete
    fun deleteUsers(users:List<User>)
    //更新多个用户
    //更新多个用户
    @Update
    fun updateUsers(users:List<User>)
    //更新用户
    @Update
    fun updateUser(user:User)

    //查询所有用户
    @Query("select * from t_user")
    fun getUsers():List<User>

     //根据Id查询用户
    @Query("select * from t_user where id = :id")
    fun  getUserById(id:Long):User

    //根据用户名模糊查询
    @Query("select * from t_user where user_name Like '%' || :name ||'%' ")
    fun getUsersByName(name:String):List<User>

}
package com.easyhi.manage.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.easyhi.manage.data.local.bean.DeviceInfo
import com.easyhi.manage.data.network.User

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(user: User)

    @Query("select * from user")
    fun getUser(): LiveData<User?>

}
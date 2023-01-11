package com.easyhi.manage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.easyhi.manage.data.local.bean.DeviceInfo

@Dao
interface DeviceDao {

    @Insert(onConflict = REPLACE)
    fun insert(deviceInfo: DeviceInfo)

    @Query("select * from deviceinfo where sn = :sn")
    fun query(sn: String): DeviceInfo?

    @Query("update deviceinfo set deviceName=:deviceName where sn = :sn")
    fun renameDevice(sn: String?, deviceName: String)

}
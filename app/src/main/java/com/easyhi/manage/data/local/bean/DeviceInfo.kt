package com.easyhi.manage.data.local.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeviceInfo(
    @PrimaryKey val sn: String,
    val deviceName: String = "",// 设备别名
    val deviceType: String = "",// 设备型号
    val merchantName: String = "",// 商户简称
    val easyId: String = "",
    val bindTime: String? = null,//绑定时间
    val clearAuthWhenExit: Boolean = true
)
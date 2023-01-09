package com.easyhi.manage.util

import com.easyhi.manage.MyApplication
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import java.util.*

object SnUtil {

    fun generateSnIfNeed(): String {
        val sn = MyApplication.sp.getString(KEY_SN, "")
        if (sn == "") {
            var newSn = DeviceIdentifier.getAndroidID(MyApplication.INSTANCE)
            if (newSn.isNullOrEmpty()) {
                newSn = UUID.randomUUID().toString().replace("-", "")
            }
            MyApplication.sp.edit().putString(KEY_SN, newSn).apply()
            return newSn
        }
        return sn!!
    }


}
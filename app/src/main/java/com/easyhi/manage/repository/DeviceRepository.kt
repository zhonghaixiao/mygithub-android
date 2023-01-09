package com.easyhi.manage.repository

import android.content.SharedPreferences
import com.easyhi.manage.data.AppDatabase
import com.easyhi.manage.data.bean.DeviceInfo
import com.easyhi.manage.net.BindInfo
import com.easyhi.manage.net.DeviceService
import com.easyhi.manage.net.Resp
import com.easyhi.manage.util.KEY_CURRENT_URL
import com.easyhi.manage.util.KEY_EASY_ID
import com.easyhi.manage.util.KEY_SN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val deviceService: DeviceService,
    private val appDatabase: AppDatabase,
    private val preferences: SharedPreferences
) {

    suspend fun getDeviceInfo(sn: String): DeviceInfo? {
        return withContext(Dispatchers.IO) {
            appDatabase.deviceDao().query(sn)
        }
    }

    suspend fun checkEasyId(easyId: String): Resp<String?> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                deviceService.checkEasyId(easyId)
            } catch (e: Exception) {
                Resp(code = -1, message = "$e")
            }
        }
    }

    suspend fun checkValidDevice(easyId: String, sn: String): Resp<BindInfo> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                deviceService.checkValidDevice(easyId, sn)
            } catch (e: Exception) {
                Resp(code = -1, message = "$e")
            }
        }
    }

    suspend fun unBind(): Resp<String> {
        return withContext(Dispatchers.IO) {
            val easyId = preferences.getString(KEY_EASY_ID, "")
            if (easyId.isNullOrEmpty()) {
                return@withContext Resp(code = -100, message = "easyId不可为空")
            }
            val sn = preferences.getString(KEY_SN, "")
            val resp = try {
                deviceService.unbind(easyId, easyId, sn!!)
            } catch (e: Exception) {
                Resp(code = -100, message = "$e")
            }
            if (resp.code == 0) {
                preferences.edit().remove(KEY_EASY_ID)
                    .remove(KEY_CURRENT_URL).apply()
            }
            return@withContext resp
        }
    }

    suspend fun renameDevice(deviceName: String): Resp<String> {
        return withContext(Dispatchers.IO) {
            val easyId = preferences.getString(KEY_EASY_ID, "")
            if (easyId.isNullOrEmpty()) {
                return@withContext Resp(code = -100, message = "easyId不可为空")
            }
            val sn = preferences.getString(KEY_SN, "")
            val resp = try{
                deviceService.renameDevice(easyId, sn!!, deviceName)
            }catch (e: Exception) {
                Resp(code = -100, message = "$e")
            }
            if (resp.code == 0) {
                appDatabase.deviceDao().renameDevice(sn, deviceName)
            }
            return@withContext resp
        }
    }


}
package com.easyhi.manage.repository

import android.content.Context
import android.os.Build
import android.system.Os
import android.util.Log
import com.easyhi.manage.MyApplication
import com.easyhi.manage.data.AppDatabase
import com.easyhi.manage.data.bean.DeviceInfo
import com.easyhi.manage.h5plugin.TAG
import com.easyhi.manage.net.AuthQrUrl
import com.easyhi.manage.net.DeviceService
import com.easyhi.manage.net.MerchantService
import com.easyhi.manage.net.Resp
import com.easyhi.manage.util.KEY_EASY_ID
import com.easyhi.manage.util.KEY_SN
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MerchantRepository @Inject constructor(
    private val deviceService: DeviceService,
    private val merchantService: MerchantService,
    private val appDatabase: AppDatabase,
    @ApplicationContext private val context: Context
) {


    suspend fun generateDeviceBindUrl(easyId: String): Resp<AuthQrUrl> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                merchantService.generateDeviceBindUrl(easyId)
            } catch (e: Exception) {
                Resp(code = -100, message = "$e")
            }
        }
    }

    suspend fun generateDeviceSettingUrl(easyId: String): Resp<AuthQrUrl> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                merchantService.generateDeviceSettingUrl(easyId)
            } catch (e: Exception) {
                Resp(code = -100, message = "$e")
            }
        }
    }

    suspend fun pollingDeviceBind(easyId: String, token: String, isBinding: Boolean): Resp<String> {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "pollingDeviceBind easyId = $easyId, isBinding = $isBinding")
            val sn = MyApplication.sp.getString(KEY_SN, "")
            val localEasyId = easyId.ifEmpty { MyApplication.sp.getString(KEY_EASY_ID, "") }
            val resp = try {
                if (isBinding){
                    merchantService.pollingDeviceBind(localEasyId!!, token, sn!!)
                } else {
                    merchantService.pollingDeviceSetting(localEasyId!!, token)
                }
            } catch (e: Exception) {
                Resp(code = -100, message = "$e")
            }
            Log.d(TAG, "pollingDeviceBind resp = $resp")
            if (resp.code == 0 && isBinding) {
                val respBind = deviceService.checkValidDevice(easyId, sn!!)
                if (respBind.code == 0) {
                    val bindingInfo = respBind.data
                    bindingInfo?.let { _bindInfo ->
                        MyApplication.sp.edit().putString(KEY_EASY_ID, easyId).apply()
                        appDatabase.deviceDao().insert(
                            DeviceInfo(
                                sn = sn,
                                deviceName = _bindInfo.deviceName ?: "",
                                deviceType = Build.MODEL,
                                merchantName = _bindInfo.merchantName ?: "",
                                easyId = easyId,
                                bindTime = _bindInfo.bindTime
                            )
                        )
                    }
                }
            }
            resp
        }
    }


}
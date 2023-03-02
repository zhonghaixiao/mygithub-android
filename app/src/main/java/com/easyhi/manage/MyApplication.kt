package com.easyhi.manage

import android.app.Application
import android.util.Log
import com.easyhi.manage.data.local.tokenDataStore
import com.easyhi.manage.serialize.AuthTokenP
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.sqlcipher.database.SQLiteDatabase


private const val TAG = "MyApplication_INIT"

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var INSTANCE: MyApplication
            private set

        var authTokenP: AuthTokenP? = null

        val hasLogin: Boolean
            get() = authTokenP != null

    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        SQLiteDatabase.loadLibs(this)

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "DeviceIdentifier.getIMEI(this) = ${DeviceIdentifier.getIMEI(this)}")
            Log.d(
                TAG,
                "DeviceIdentifier.getAndroidID(this) = ${DeviceIdentifier.getAndroidID(this)}"
            )
            Log.d(TAG, "DeviceIdentifier.getWidevineID() = ${DeviceIdentifier.getWidevineID()}")
            Log.d(TAG, "DeviceIdentifier.getPseudoID() = ${DeviceIdentifier.getPseudoID()}")
            Log.d(TAG, "DeviceIdentifier.getGUID(this) = ${DeviceIdentifier.getGUID(this)}")
            Log.d(TAG, "DeviceID.supportedOAID(this) = ${DeviceID.supportedOAID(this)}")
            Log.d(TAG, "DeviceIdentifier.getOAID(this) = ${DeviceIdentifier.getOAID(this)}")
            DeviceID.getOAID(this, object : IGetter {
                override fun onOAIDGetComplete(result: String) {
                    // 不同厂商的OAID/AAID格式是不一样的，可进行MD5、SHA1之类的哈希运算统一
                    Log.d(TAG, "onOAIDGetComplete = $result")
                }

                override fun onOAIDGetError(error: Exception) {
                    // 获取OAID/AAID失败
                    Log.d(TAG, "onOAIDGetError = $error")
                }
            })
        }

        GlobalScope.launch {
            tokenDataStore.data.collectLatest { authToken ->
                authTokenP = authToken
            }
        }

    }

    override fun onTerminate() {
        super.onTerminate()

    }


}
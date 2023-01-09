package com.easyhi.manage

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.easyhi.manage.MyApplication.Companion.sp
import com.easyhi.manage.util.*
import com.easyhi.manage.util.printer.SunmiPrintHelper
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import dagger.hilt.android.HiltAndroidApp
import net.sqlcipher.database.SQLiteDatabase


private const val TAG = "MyApplication_INIT"

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var INSTANCE: MyApplication
            private set

        lateinit var sp: SharedPreferences

    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        SQLiteDatabase.loadLibs(this)
        SpUtil.init(this)
        sp = SpUtil.getSp()

        SnUtil.generateSnIfNeed()

        DeviceIdentifier.register(this)

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "DeviceIdentifier.getIMEI(this) = ${DeviceIdentifier.getIMEI(this)}")
            Log.d(TAG, "DeviceIdentifier.getAndroidID(this) = ${DeviceIdentifier.getAndroidID(this)}")
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
        initX5()
//        QbSdk.initX5Environment(this, object: QbSdk.PreInitCallback{
//            override fun onCoreInitFinished() {
//                Log.d(TAG, "onCoreInitFinished ")
//            }
//
//            override fun onViewInitFinished(isX5: Boolean) {
//                Log.d(TAG, "onViewInitFinished isX5 = $isX5")
//            }
//
//        })
//        // 在调用TBS初始化、创建WebView之前进行如下配置
//        val map = mutableMapOf<String, Any>()
//        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
//        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
//        QbSdk.initTbsSettings(map)

        SunmiPrintHelper.getInstance().initSunmiPrinterService(this)
    }

    private fun initX5() {
        if (QbSdk.getTbsVersion(applicationContext) == 0) {
            Log.i(TAG, "Install")
            val phoneCpus = Build.SUPPORTED_ABIS
            var fileName = ""
            if (phoneCpus[0] == "arm64-v8a") {
                fileName = "046007_x5.tbs.apk"
                Log.i(TAG, "64bit $fileName")
            } else if (phoneCpus[0] == "armeabi-v7a") {
                fileName = "045912_x5.tbs.apk"
                Log.i(TAG, "32bit $fileName")
            }
            if (fileName.isBlank()) {
                Log.i(TAG, "no support")
                initX5Setting()
            }
            val newPath = FileUtil.getAssetsCacheFile(applicationContext, fileName)
            Log.i(TAG, "path $newPath")
            QbSdk.reset(applicationContext)
            QbSdk.installLocalTbsCore(applicationContext, fileName.substring(0, fileName.indexOf("_")).toInt(), newPath)
            QbSdk.setTbsListener(object : TbsListener {
                override fun onDownloadFinish(i: Int) {}
                override fun onInstallFinish(i: Int) {
                    Log.i(TAG, "onInstallFinish: $i")
                    initX5Setting()
                }

                override fun onDownloadProgress(i: Int) {}
            })
        } else {
            Log.d(TAG, "onlyInit")
            initX5Setting()
        }
    }

    private fun initX5Setting() {
        //避免第一次打开需要安转X5，设置的的监听导致内存泄漏，重置为null
        QbSdk.setTbsListener(null)

        val map = HashMap<String, Any>(2)
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)

        Log.d(
            TAG, "canLoadX5: ${QbSdk.canLoadX5(applicationContext)}|TbsVersion:" + QbSdk.getTbsVersion(
                applicationContext
            )
        )
    }

    override fun onTerminate() {
        super.onTerminate()

        SunmiPrintHelper.getInstance().deInitSunmiPrinterService(this)

    }


}
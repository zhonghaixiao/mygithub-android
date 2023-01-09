package com.easyhi.manage.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SpUtil {

    private var _sp: SharedPreferences? = null

    fun init(context: Context) {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        _sp = EncryptedSharedPreferences.create(
            context,
            "encrypted_contents",
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getSp(): SharedPreferences {
        return _sp!!
    }

}

//var Context.sp : SharedPreferences by lazy {
//    SpUtil.getSp()
//}



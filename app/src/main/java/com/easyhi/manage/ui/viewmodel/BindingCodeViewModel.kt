package com.easyhi.manage.ui.viewmodel

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhi.manage.net.Resp
import com.easyhi.manage.repository.DeviceRepository
import com.easyhi.manage.repository.MerchantRepository
import com.easyhi.manage.util.KEY_EASY_ID
import com.easyhi.manage.util.QrUtil
import com.easyhi.manage.util.SnUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class BindingCodeViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val merchantRepository: MerchantRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableLiveData<BindingUiState>()
    val uiState: LiveData<BindingUiState> = _uiState

    private var pollJob: Job? = null
    var isBinding = true

    /**
     * 生成绑定二维码
     */
    fun generateDeviceBindUrl(easyId: String, sn: String) {
        viewModelScope.launch {
            val resp = merchantRepository.generateDeviceBindUrl(easyId)
            if (resp.code == 0 && resp.data != null) {
                isBinding = true
                val targetQrUrl = "${resp.data.qrUrl}/${sn}"
                val qrBitmap = withContext(Dispatchers.IO) {
                    QrUtil.zxing(targetQrUrl, 500, 500)
                }
                _uiState.value = BindingStateInit(
                    qrUrl = targetQrUrl,
                    token = resp.data.token,
                    sn = sn,
                    qrBitmap = qrBitmap
                )
            } else {
                _uiState.value = BindingStateError(resp.message)
            }
        }
    }

    /**
     * 生成授权二维码
     */
    fun generateAuthUrl(sn: String) {
        viewModelScope.launch {
            val easyId = preferences.getString(KEY_EASY_ID, "")
            val resp = merchantRepository.generateDeviceSettingUrl(easyId!!)
            if (resp.code == 0 && resp.data != null) {
                isBinding = false
                val targetQrUrl = "${resp.data.qrUrl}/${sn}"
                val qrBitmap = withContext(Dispatchers.IO) {
                    QrUtil.zxing(targetQrUrl, 500, 500)
                }
                _uiState.value = BindingStateInit(
                    qrUrl = targetQrUrl,
                    token = resp.data.token,
                    sn = sn,
                    qrBitmap = qrBitmap
                )
            } else {
                _uiState.value = BindingStateError(resp.message)
            }
        }
    }


    /**
     * 查询二维码扫码状态
     */
    fun pollDeviceBinding(easyId: String, token: String) {
        pollJob?.cancel()
        pollJob = viewModelScope.launch {
            do {
                doPollDeviceBinding(easyId, token)
                delay(2000)
            } while (true)
        }
    }

    fun cancelPoll() {
        pollJob?.cancel()
    }

    private fun doPollDeviceBinding(easyId: String, token: String) {
        viewModelScope.launch {
            val resp = merchantRepository.pollingDeviceBind(easyId, token, isBinding)
            Log.d("BindingCode", "doPollDeviceBinding resp = $resp")
            when (resp.code) {
                0 -> {
                    pollJob?.cancel()
                    val sn = SnUtil.generateSnIfNeed()
                    _uiState.value = BindingStateConfirmed(sn = sn, easyId = easyId)
                }
                -2 -> {
                    _uiState.value = BindingStateExpired
                    pollJob?.cancel()
                }
                -100 -> {
                    pollJob?.cancel()
                    _uiState.value = BindingStateError(resp.message)
                }
                else -> {
                    _uiState.value = BindingStateWaitConfirm
                }
            }
        }
    }

    fun unbindDevice(callback: (Resp<String>) -> Unit) {
        viewModelScope.launch {
            val resp = deviceRepository.unBind()
            callback(resp)
        }
    }
}

sealed interface BindingUiState

data class BindingStateInit(
    val qrUrl: String,
    val token: String,
    val sn: String,
    val qrBitmap: Bitmap?
) : BindingUiState

//object BindingStateWaitScan : BindingUiState

object BindingStateWaitConfirm : BindingUiState

data class BindingStateConfirmed(
    val easyId: String,
    val sn: String
) : BindingUiState

object BindingStateExpired : BindingUiState

data class BindingStateError(
    val error: String
) : BindingUiState

//data class BindingCodeUiState(
//    val state: QrState = QrState.INIT,
//    val
//)

enum class QrState {
    INIT, WAIT_SCAN, WAIT_CONFIRM, CONFIRMED, EXPIRED
}






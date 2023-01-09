package com.easyhi.manage.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhi.manage.data.bean.DeviceInfo
import com.easyhi.manage.net.Resp
import com.easyhi.manage.repository.DeviceRepository
import com.easyhi.manage.repository.MerchantRepository
import com.easyhi.manage.util.KEY_SN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val merchantRepository: MerchantRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _deviceInfoData = MutableLiveData<DeviceInfo>()
    val deviceInfoData: LiveData<DeviceInfo> = _deviceInfoData

    fun initDeviceInfo(){
        viewModelScope.launch {
            val sn = preferences.getString(KEY_SN, "")
            if (sn.isNullOrEmpty()) {
                return@launch
            }
            val deviceInfo = deviceRepository.getDeviceInfo(sn)
            deviceInfo?.let { _deviceInfo ->
                _deviceInfoData.value = _deviceInfo
            }
        }
    }

    fun renameDevice(deviceName: String, callback: (Resp<String>)->Unit) {
        viewModelScope.launch {
            val resp = deviceRepository.renameDevice(deviceName)
            if (resp.code == 0) {
                initDeviceInfo()
            }
            callback(resp)
        }
    }


}









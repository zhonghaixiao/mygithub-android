package com.easyhi.manage.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhi.manage.net.BindInfo
import com.easyhi.manage.net.Resp
import com.easyhi.manage.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val COUNT = 60

@HiltViewModel
class BindingViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {

    private val _countData = MutableLiveData(COUNT)
    val countData: LiveData<Int> = _countData
    var countJob: Job? = null

    fun checkEasyId(easyId: String, callback: (Resp<String?>) -> Unit) {
        viewModelScope.launch {
            val resp = deviceRepository.checkEasyId(easyId)
            callback(resp)
        }
    }

    fun checkBindEasyId(easyId: String, sn: String, callback: (Resp<BindInfo>) -> Unit) {
        viewModelScope.launch {
            val resp = deviceRepository.checkValidDevice(easyId, sn)
            callback(resp)
        }
    }

    fun beginCount() {
        viewModelScope.launch {
            _countData.value = COUNT
            countJob?.cancel()
            // 更新倒计时
            countJob = launch(Dispatchers.IO) {
                do {
                    val curData = (countData.value ?: COUNT) - 1
                    _countData.postValue(curData)
                    delay(1000)
                } while (curData > 0)
            }
        }
    }

    fun resetCount() {
        viewModelScope.launch {
            _countData.value = 60
            countJob?.cancel()
        }
    }

}




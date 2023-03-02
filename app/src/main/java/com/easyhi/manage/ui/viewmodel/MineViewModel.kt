package com.easyhi.manage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhi.manage.data.network.User
import com.easyhi.manage.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userBaseInfoData = userRepository.getCurrentUser()

    fun pullUserBaseInfo() {
        viewModelScope.launch {
            userRepository.getUserInfoFromRemote()
        }
    }


}





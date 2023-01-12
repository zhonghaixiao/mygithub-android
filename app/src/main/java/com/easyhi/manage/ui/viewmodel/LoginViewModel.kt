package com.easyhi.manage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhi.manage.repository.LoginRepository
import com.easyhi.manage.ui.state.LoginUiState
import com.easyhi.manage.util.KEY_RANDOM_STATE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import org.apache.commons.lang3.RandomStringUtils
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState = _uiState.asStateFlow()

    fun getAuthToken(code: String) {
        viewModelScope.launch {
            _uiState.getAndUpdate {
                it.copy(isLoading = true)
            }
            val getTokenResult = loginRepository.getAuthToken(code)
            if (getTokenResult.errorMessage != null) {
                _uiState.getAndUpdate {
                    it.copy(
                        isLoading = false,
                        errorMessage = getTokenResult.errorMessage,
                        authSuccess = false
                    )
                }
            } else if (getTokenResult.token != null) {
                _uiState.getAndUpdate {
                    it.copy(
                        isLoading = false,
                        authSuccess = true
                    )
                }
            }
        }
    }


}





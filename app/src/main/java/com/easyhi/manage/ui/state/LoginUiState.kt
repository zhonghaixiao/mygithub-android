package com.easyhi.manage.ui.state

data class LoginUiState(
    val isLoading: Boolean = false,
    val authSuccess: Boolean = false,
    val errorMessage: String? = null
)
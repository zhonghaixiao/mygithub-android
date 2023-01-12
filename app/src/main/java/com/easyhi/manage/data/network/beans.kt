package com.easyhi.manage.data.network

data class Resp<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
)

data class AuthQrUrl(
    val qrUrl: String,
    val token: String
)

data class BindInfo(
    val bindTime: String?,
    val deviceName: String?,
    val merchantName: String?
)

data class AuthToken(
    val accessToken: String,
    val scope: String,
    val tokenType: String
)

data class AuthTokenResult(
    val errorMessage: String? = null,
    val token: AuthToken? = null
)











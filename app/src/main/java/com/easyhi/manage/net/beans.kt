package com.easyhi.manage.net

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














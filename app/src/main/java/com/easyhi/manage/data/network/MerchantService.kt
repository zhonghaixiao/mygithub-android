package com.easyhi.manage.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MerchantService {

    /**
     * 生成设备绑定的url
     */
    @GET("base/manage/AppDeviceController/generateDeviceBindUrl")
    suspend fun generateDeviceBindUrl(@Header("tcode") tcode: String): Resp<AuthQrUrl>

    /**
     * 检验easyID和设备的绑定关系
     */
    @GET("base/manage/AppDeviceController/pollingDeviceBind/{token}/{sn}")
    suspend fun pollingDeviceBind(
        @Header("tcode") tcode: String,
        @Path("token") token: String,
        @Path("sn") sn: String
    ): Resp<String>

    @GET("base/manage/AppDeviceController/generateDeviceSettingUrl")
    suspend fun generateDeviceSettingUrl(@Header("tcode") tcode: String): Resp<AuthQrUrl>

    @GET("base/manage/AppDeviceController/pollingDeviceSetting/{token}")
    suspend fun pollingDeviceSetting(
        @Header("tcode") tcode: String,
        @Path("token") token: String,
    ): Resp<String>

}
package com.easyhi.manage.data.network

import retrofit2.http.*

interface DeviceService {

    /**
     * 校验easyID是否有效
     */
    @GET("app/manage/DeviceController/checkEasyId/{easyId}")
    suspend fun checkEasyId(@Path("easyId") easyId: String): Resp<String?>

    /**
     * 检验easyID和设备的绑定关系
     */
    @POST("app/manage/DeviceController/checkValidDevice/{easyId}/{sn}")
    suspend fun checkValidDevice(
        @Path("easyId") easyId: String,
        @Path("sn") sn: String
    ): Resp<BindInfo>

    /**
     * 解绑设备
     */
    @POST("app/manage/DeviceController/unbind/{easyId}/{sn}")
    suspend fun unbind(
        @Header("tcode") tcode: String,
        @Path("easyId") easyId: String,
        @Path("sn") sn: String
    ): Resp<String>

    /**
     * 修改设备别名
     */
    @FormUrlEncoded
    @POST("app/manage/DeviceController/renameDevice")
    suspend fun renameDevice(
        @Field("easyId") easyId: String,
        @Field("sn") sn: String, @Field("deviceName") deviceName: String
    ): Resp<String>

    /**
     * 上报设备类型
     */
    @FormUrlEncoded
    @POST("app/manage/DeviceController/reportDevice")
    suspend fun reportDevice(
        @Field("easyId") easyId: String,
        @Field("sn") sn: String,
        @Field("deviceModel") deviceModel: String
    ): Resp<String>

}
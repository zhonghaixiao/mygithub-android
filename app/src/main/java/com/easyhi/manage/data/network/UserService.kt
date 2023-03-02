package com.easyhi.manage.data.network

import retrofit2.http.GET

interface UserService {

    @GET("/user")
    suspend fun findUserInfo(): User


}






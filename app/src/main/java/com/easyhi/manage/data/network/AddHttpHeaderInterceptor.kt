package com.easyhi.manage.data.network

import com.easyhi.manage.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class AddHttpHeaderInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return if (original.url.encodedPath == "/login/oauth/access_token") {
            val request = original.newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        } else {
            val accessToken = MyApplication.authTokenP?.accessToken ?: ""
            val request = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(request)
        }
    }

}
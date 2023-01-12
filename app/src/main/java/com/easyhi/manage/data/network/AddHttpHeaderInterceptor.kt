package com.easyhi.manage.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AddHttpHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}
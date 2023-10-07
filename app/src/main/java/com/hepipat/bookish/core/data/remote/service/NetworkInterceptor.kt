package com.hepipat.bookish.core.data.remote.service

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = ""
        val request: Request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", accessToken)
                .build()
        return chain.proceed(request)
    }
}
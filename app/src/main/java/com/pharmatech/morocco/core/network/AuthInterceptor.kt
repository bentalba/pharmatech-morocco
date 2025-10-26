package com.pharmatech.morocco.core.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor : Interceptor {
    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        this.authToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        authToken?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        requestBuilder
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")

        val request = requestBuilder.build()
        Timber.d("API Request: ${request.method} ${request.url}")

        return chain.proceed(request)
    }
}


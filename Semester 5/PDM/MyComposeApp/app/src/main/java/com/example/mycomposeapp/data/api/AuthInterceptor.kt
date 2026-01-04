package com.example.mycomposeapp.data.api

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: suspend () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider() }

        Log.d("AuthInterceptor", "Token-ul meu este: $token")

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("app-auth", token)
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
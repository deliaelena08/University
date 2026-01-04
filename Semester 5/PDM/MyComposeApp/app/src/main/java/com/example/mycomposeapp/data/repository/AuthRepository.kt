package com.example.mycomposeapp.data.repository

import com.example.mycomposeapp.data.api.ApiService
import com.example.mycomposeapp.data.model.LoginRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    fun isUserLoggedIn(): Boolean {
         val token = runBlocking { tokenManager.token.first() }
        return !token.isNullOrEmpty()
    }

    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(username, password))

            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token

                tokenManager.saveToken(token)

                Result.success(Unit)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        try {
            apiService.logout()
        } catch (e: Exception) {
        } finally {
            tokenManager.clearToken()
        }
    }
}
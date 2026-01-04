package com.example.mycomposeapp.data.model

import java.time.LocalDateTime


data class User(
    val id: Long,
    val username: String,
    val password: String,
    val lastActive: LocalDateTime? = null,
    val active: Boolean = false
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class SignupRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

data class SignupResponse(
    val id: Long,
    val username: String,
    val token: String
)
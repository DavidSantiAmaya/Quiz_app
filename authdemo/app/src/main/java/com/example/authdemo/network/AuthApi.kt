package com.example.authdemo.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body creds: LoginRequest): Response<LoginResponse>
}
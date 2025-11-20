package com.example.authdemo.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // ← CAMBIO AQUÍ

object RetrofitClient {
    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().addInterceptor(logger).build()

    val api: AuthApi = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()) // ← CAMBIO AQUÍ
        .build()
        .create(AuthApi::class.java)
}
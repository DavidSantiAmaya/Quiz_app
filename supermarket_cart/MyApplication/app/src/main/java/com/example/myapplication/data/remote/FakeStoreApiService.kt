package com.example.myapplication.data.remote

import com.example.myapplication.model.ProductDto
import retrofit2.http.GET




interface FakeStoreApiService {


    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}

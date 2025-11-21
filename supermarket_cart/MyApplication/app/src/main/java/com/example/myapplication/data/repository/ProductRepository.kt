package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.FakeStoreApiService
import com.example.myapplication.data.remote.NetworkModule
import com.example.myapplication.model.ProductDto

class ProductRepository {
    // Usa NetworkModule en lugar de crear Retrofit.Builder() inline
    private val api: FakeStoreApiService = NetworkModule.createService()

    suspend fun getProducts(): List<ProductDto> = api.getProducts()
}

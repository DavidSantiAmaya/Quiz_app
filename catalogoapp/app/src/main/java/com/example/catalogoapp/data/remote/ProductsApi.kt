package com.example.catalogoapp.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

data class ProductDto(val id:Int, val title:String, val description:String, val price:Double, val image:String)

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET
    suspend fun downloadImage(@Url url:String): Response<ResponseBody>

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}

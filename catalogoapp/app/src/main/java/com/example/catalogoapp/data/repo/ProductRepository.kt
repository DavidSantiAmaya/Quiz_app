
package com.example.catalogoapp.data.repo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.catalogoapp.data.remote.ProductsApi
import com.example.catalogoapp.data.local.AppDatabase
import com.example.catalogoapp.data.local.ProductEntity
import com.example.catalogoapp.data.local.CartItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class ProductRepository(private val api: ProductsApi, private val db: AppDatabase) {

    suspend fun fetchAndCacheProducts(): List<ProductEntity> = withContext(Dispatchers.IO) {
        val dtos = api.getProducts()
        val entities = dtos.map { d -> ProductEntity(d.id, d.title, d.description, d.price, d.image) }
        db.productDao().insertAll(entities)
        entities
    }

    suspend fun getCachedProducts(): List<ProductEntity> = withContext(Dispatchers.IO) {
        db.productDao().getAll()
    }

    suspend fun downloadImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val resp = api.downloadImage(url)
            if (resp.isSuccessful) {
                val body: ResponseBody? = resp.body()
                body?.byteStream()?.use { stream ->
                    return@withContext BitmapFactory.decodeStream(stream)
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun upsertCartItem(productId: Int, title: String, price: Double, qty: Int) = withContext(Dispatchers.IO) {
        db.cartDao().upsert(CartItemEntity(productId = productId, title = title, price = price, qty = qty))
    }
}

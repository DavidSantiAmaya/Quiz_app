
package com.example.catalogoapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogoapp.data.remote.NetworkModule
import com.example.catalogoapp.data.local.AppDatabase
import com.example.catalogoapp.data.repo.ProductRepository
import com.example.catalogoapp.data.local.ProductEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductsUiState(val loading:Boolean=false, val products:List<ProductEntity> = emptyList(), val error:String = "")

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val api = NetworkModule.provideProductsApi()
    private val db = AppDatabase.getInstance(application)
    private val repo = ProductRepository(api, db)

    private val _ui = MutableStateFlow(ProductsUiState(loading=true))
    val ui: StateFlow<ProductsUiState> = _ui

    init {
        viewModelScope.launch {
            try {
                val cached = repo.getCachedProducts()
                if (cached.isNotEmpty()) {
                    _ui.value = ProductsUiState(loading=false, products=cached)
                }
                val fresh = repo.fetchAndCacheProducts()
                _ui.value = ProductsUiState(loading=false, products=fresh)
            } catch (e: Exception) {
                _ui.value = ProductsUiState(loading=false, products=emptyList(), error = e.message ?: "Error")
            }
        }
    }

    fun downloadImage(url:String, onResult:(android.graphics.Bitmap?)->Unit) {
        viewModelScope.launch {
            val bmp = repo.downloadImage(url)
            onResult(bmp)
        }
    }

    fun addToCart(productId:Int, title:String, price:Double, qty:Int) {
        viewModelScope.launch {
            repo.upsertCartItem(productId, title, price, qty)
        }
    }
}

package com.example.myapplication.data.repository

import android.app.Application
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.CartItem
import com.example.myapplication.model.ProductDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val app: Application) {
    private val dao = AppDatabase.getInstance(app).cartDao()

    fun getCartItems(): Flow<List<CartItem>> = dao.getAllCartItems()

    fun getCartTotal(): Flow<Double> = dao.getAllCartItems().map { list -> list.sumOf { it.subtotal } }

    suspend fun addItemToCart(product: ProductDto, qty: Int) {
        val existing = dao.getCartItem(product.id)
        val newQty = (existing?.quantity ?: 0) + qty
        val newSubtotal = product.price * newQty
        val item = CartItem(
            productId = product.id,
            title = product.title,
            price = product.price,
            image = product.image,
            quantity = newQty,
            subtotal = newSubtotal
        )
        dao.insertCartItem(item)
    }

    suspend fun updateQuantity(productId: Int, qty: Int) {
        val existing = dao.getCartItem(productId) ?: return
        val newSubtotal = existing.price * qty
        dao.insertCartItem(existing.copy(quantity = qty, subtotal = newSubtotal))
    }

    suspend fun removeItem(item: CartItem) {
        dao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }
}

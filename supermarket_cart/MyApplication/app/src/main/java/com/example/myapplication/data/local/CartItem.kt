package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int,
    val subtotal: Double
)


package com.example.catalogoapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
  @PrimaryKey val id: Int,
  val title: String,
  val description: String,
  val price: Double,
  val imageUrl: String
)

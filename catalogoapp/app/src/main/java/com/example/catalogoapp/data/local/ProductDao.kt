
package com.example.catalogoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
  @Query("SELECT * FROM products")
  suspend fun getAll(): List<ProductEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<ProductEntity>)
}

package com.example.myapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.CartRepository
import com.example.myapplication.model.ProductDto
import com.example.myapplication.model.RatingDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repo: CartRepository
    private lateinit var app: android.app.Application

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(app, AppDatabase::class.java).build()
        repo = CartRepository(app)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testAddItemAndTotal() = runBlocking {
        val product = ProductDto(1, "X", 5.0, "Desc", "cat", "url", RatingDto(4.0,10))
        repo.addItemToCart(product, 3)
        val total = repo.getCartTotal().first()
        Assert.assertEquals(15.0, total, 0.01)
    }
}

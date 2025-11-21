package com.example.myapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.CartItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CartDaoTest {
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testInsertAndRetrieve() = runBlocking {
        val dao = db.cartDao()
        val item = CartItem(1, "Prueba", 10.0, "url", 2, 20.0)
        dao.insertCartItem(item)
        val items = dao.getAllCartItems().first()
        Assert.assertEquals(1, items.size)
        Assert.assertEquals(item.productId, items[0].productId)
    }
}

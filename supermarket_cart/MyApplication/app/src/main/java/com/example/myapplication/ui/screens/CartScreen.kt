package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.repository.CartRepository
import com.example.myapplication.viewmodel.CartViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(onCheckout: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { CartViewModel(context.applicationContext as android.app.Application) }
    val cartItems by viewModel.cartItems.collectAsState()
    val total by viewModel.totalAmount.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = { TopAppBar(title = { Text("Carrito") }) }) { padding ->
        Column(modifier = Modifier.padding(8.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemComposable(item = item, onRemove = {
                        viewModel.removeItem(item)
                    }, onUpdateQty = { newQty ->
                        if (newQty > 0) viewModel.updateQuantity(item.productId, newQty)
                    })
                }
            }
            Text("Total: $ $total", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row {
                Button(onClick = {
                    coroutineScope.launch {
                        viewModel.clearCart()
                        onCheckout()
                    }
                }) {
                    Text("Pagar")
                }
            }
        }
    }
}

// Small wrapper composable to avoid name collision with entity
@Composable
fun CartItemComposable(item: com.example.myapplication.data.local.CartItem, onRemove: () -> Unit, onUpdateQty: (Int) -> Unit) {
    com.example.myapplication.ui.screens.CartItem(item = item, onRemove = onRemove, onUpdateQty = onUpdateQty)
}

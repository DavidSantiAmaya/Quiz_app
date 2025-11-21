package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.ProductListViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R
import androidx.compose.foundation.lazy.items
import com.example.myapplication.model.ProductDto
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.example.myapplication.data.repository.CartRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(onLogout: () -> Unit, onOpenCart: () -> Unit = {}, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { ProductListViewModel() }
    val uiState by viewModel.productListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val cartRepo = CartRepository(context.applicationContext as android.app.Application)

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Productos") },
            actions = {
                IconButton(onClick = onOpenCart) {
                    Icon(painter = painterResource(id = android.R.drawable.ic_menu_myplaces), contentDescription = "Carrito")
                }
            }
        )
    }) { padding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize()) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
            }
            uiState.error.isNotEmpty() -> {
                Text(uiState.error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(padding))
            }
            else -> {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(uiState.products) { product ->
                        ProductItem(product = product) { qty ->
                            coroutineScope.launch {
                                cartRepo.addItemToCart(product, qty)
                            }
                        }
                    }
                }
            }
        }
    }
}

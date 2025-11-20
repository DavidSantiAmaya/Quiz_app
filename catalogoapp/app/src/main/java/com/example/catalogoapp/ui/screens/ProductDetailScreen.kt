package com.example.catalogoapp.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catalogoapp.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: ProductViewModel = viewModel()
    val state by vm.ui.collectAsState()
    val product = state.products.find { it.id == productId }

    // Local states for image and quantity
    var bmp by remember { mutableStateOf<Bitmap?>(null) }
    var loadingImage by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(1) }
    val canAdd = quantity > 0

    // Download image once per product
    LaunchedEffect(productId, product?.imageUrl) {
        if (product != null && !loadingImage && bmp == null) {
            loadingImage = true
            vm.downloadImage(product.imageUrl) {
                bmp = it
                loadingImage = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product?.title ?: "Detalle") },
                navigationIcon = {
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (product == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Producto no encontrado", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = onBack) { Text("Volver") }
                }
                return@Scaffold
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Imagen principal
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (bmp != null) {
                            Image(
                                bitmap = bmp!!.asImageBitmap(),
                                contentDescription = product.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Placeholder while loading or if failed
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                if (loadingImage) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title, short description and price
                Text(product.title, style = MaterialTheme.typography.titleLarge, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                Text(product.description, style = MaterialTheme.typography.bodyMedium, maxLines = 4, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "$ ${product.price}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(20.dp))

                // Quantity selector and Add button
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    // Quantity controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(48.dp)
                            .padding(end = 8.dp)
                    ) {
                        Button(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("-", style = MaterialTheme.typography.titleLarge)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(quantity.toString(), style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { quantity++ },
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+", style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    // Add button
                    Button(
                        onClick = {
                            if (canAdd) {
                                vm.addToCart(product.id, product.title, product.price, quantity)
                                onBack()
                            }
                        },
                        enabled = canAdd,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Agregar ($quantity)")
                    }
                }
            }
        }
    }
}

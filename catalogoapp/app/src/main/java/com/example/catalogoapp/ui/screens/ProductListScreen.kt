package com.example.catalogoapp.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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

@Composable
fun ProductListScreen(
    onOpenDetail: (Int) -> Unit,
    onBack: (() -> Unit)? = null, // si no quieres back, pasa null
    modifier: Modifier = Modifier
) {
    val vm: ProductViewModel = viewModel()
    val state by vm.ui.collectAsState()

    Scaffold(
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (state.loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            if (state.error.isNotEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                items(state.products) { p ->
                    ProductCard(
                        product = p,
                        onClick = { onOpenDetail(p.id) },
                        vm = vm
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: com.example.catalogoapp.data.local.ProductEntity,
    onClick: () -> Unit,
    vm: ProductViewModel
) {
    // estado local para el bitmap de la imagen
    var bmp by remember(product.id, product.imageUrl) { mutableStateOf<Bitmap?>(null) }
    // pequeño flag para evitar múltiples llamadas simultaneas
    var loadingImage by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = product.imageUrl) {
        if (!loadingImage && bmp == null) {
            loadingImage = true
            vm.downloadImage(product.imageUrl) { loaded ->
                bmp = loaded
                loadingImage = false
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen con esquinas redondeadas
            if (bmp != null) {
                Image(
                    bitmap = bmp!!.asImageBitmap(),
                    contentDescription = product.title,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$ ${product.price}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.model.ProductDto
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ProductItem(product: ProductDto, onAdd: (Int) -> Unit) {
    var quantity by remember { mutableStateOf(1) }
    Card(Modifier.padding(8.dp).fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(Modifier.padding(8.dp)) {
            AsyncImage(model = product.image, contentDescription = product.title, modifier = Modifier.size(80.dp))
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.title, style = MaterialTheme.typography.titleMedium)
                Text(product.description.take(80) + "...", style = MaterialTheme.typography.bodySmall)
                Text("$ ${product.price}", style = MaterialTheme.typography.bodyMedium)
                Row {
                    Button(onClick = { if (quantity > 1) quantity-- }) { Text("-") }
                    OutlinedTextField(
                        value = quantity.toString(),
                        onValueChange = { new -> quantity = new.filter { it.isDigit() }.toIntOrNull() ?: 1 },
                        modifier = Modifier.width(64.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(onClick = { quantity++ }) { Text("+") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onAdd(quantity) }) { Text("Agregar") }
                }
            }
        }
    }
}

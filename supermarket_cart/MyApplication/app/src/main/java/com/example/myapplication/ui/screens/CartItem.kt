package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CartItem(item: com.example.myapplication.data.local.CartItem, onRemove: () -> Unit, onUpdateQty: (Int) -> Unit) {
    var qty by remember { mutableStateOf(item.quantity) }
    Card(Modifier.padding(4.dp).fillMaxWidth()) {
        Row(Modifier.padding(8.dp)) {
            AsyncImage(model = item.image, contentDescription = item.title, modifier = Modifier.size(60.dp))
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.bodyLarge)
                Text("Precio: $${item.price}")
                Row {
                    Button(onClick = { if (qty > 1) { qty--; onUpdateQty(qty) } }) { Text("-") }
                    Text(qty.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                    Button(onClick = { qty++; onUpdateQty(qty) }) { Text("+") }
                    Spacer(Modifier.width(8.dp))
                    Text("Subtotal: $${item.subtotal}")
                }
            }
            IconButton(onClick = onRemove) {
                Icon(painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_delete), contentDescription = "Eliminar")
            }
        }
    }
}

package com.example.authdemo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.authdemo.data.AuthRepository
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(repo: AuthRepository, onLogout: () -> Unit = {}, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    Column(modifier = modifier.padding(20.dp)) {
        Text("Sesión iniciada", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                repo.logout()
                onLogout()
            }
        }) {
            Text("Cerrar sesión")
        }
    }
}
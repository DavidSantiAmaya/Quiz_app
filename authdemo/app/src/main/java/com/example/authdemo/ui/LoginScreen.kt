package com.example.authdemo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.authdemo.data.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(repo: AuthRepository, modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf("mor_2314") }
    var password by remember { mutableStateOf("83r5^_") }
    var error by remember { mutableStateOf("") }
    var debugInfo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.padding(20.dp)) {
        Text("AuthDemo", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                error = ""
                debugInfo = ""
                if (username.isBlank() || password.isBlank()) {
                    error = "Ingrese usuario y contraseña"
                    return@Button
                }

                isLoading = true
                scope.launch {
                    try {
                        val ok = repo.login(username.trim(), password.trim())
                        debugInfo = repo.lastError // ← MUESTRA EL ERROR DETALLADO
                        isLoading = false

                        if (!ok) {
                            error = "Login falló"
                        }
                    } catch (e: Exception) {
                        debugInfo = "Error: ${e.message}"
                        error = "Error: ${e.message}"
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        if (debugInfo.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Debug: $debugInfo",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

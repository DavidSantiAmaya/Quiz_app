
package com.example.authdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.authdemo.data.AuthRepository
import com.example.authdemo.data.DataStoreManager
import com.example.authdemo.network.RetrofitClient
import com.example.authdemo.ui.HomeScreen
import com.example.authdemo.ui.LoginScreen
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ds = DataStoreManager(this)
        val repo = AuthRepository(RetrofitClient.api, ds)

        setContent {
            var token by remember { mutableStateOf<String?>(null) }

            // collect token flow - auto-login if token exists
            LaunchedEffect(Unit) {
                repo.tokenFlow().collectLatest {
                    token = it.ifBlank { null }
                }
            }

            Surface(color = MaterialTheme.colorScheme.background) {
                if (token != null) {
                    HomeScreen(onLogout = {
                        // logout will clear token in DataStore; token flow will update and UI will switch to login
                    }, repo = repo)
                } else {
                    LoginScreen(repo = repo)
                }
            }
        }
    }
}

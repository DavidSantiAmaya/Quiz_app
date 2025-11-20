
package com.example.catalogoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.catalogoapp.ui.theme.CatalogoTheme
import com.example.catalogoapp.ui.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalogoTheme {
                NavGraph()
            }
        }
    }
}

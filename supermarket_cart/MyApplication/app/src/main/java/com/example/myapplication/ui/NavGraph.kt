package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.CartScreen
import com.example.myapplication.ui.screens.ProductListScreen

/**
 * NavGraph sólo con dos destinos:
 *  - "products" : pantalla con la lista de productos (inicio)
 *  - "cart"     : pantalla del carrito de compras
 */
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    // Controlador de navegación que mantiene el back stack
    val navController = rememberNavController()

    // NavHost que declara las rutas disponibles y el destino inicial
    NavHost(
        navController = navController,
        startDestination = "products", // iniciamos en la lista de productos
        modifier = modifier
    ) {
        // Ruta: lista de productos
        composable("products") {
            ProductListScreen(
                // Cuando el usuario cierra sesión o se quiere volver a "login",
                // aquí podrías manejarlo (si lo necesitas) — lo omitimos intencionalmente
                // porque pediste solo productos y carrito.
                onLogout = {
                    // ejemplo: si quisieras limpiar backstack y volver a "products"
                    navController.navigate("products") {
                        popUpTo("products") { inclusive = true }
                    }
                },
                // Abrir la pantalla del carrito
                onOpenCart = {
                    navController.navigate("cart")
                }
            )
        }

        // Ruta: carrito de compras
        composable("cart") {
            CartScreen(
                // Al finalizar checkout volvemos a la lista de productos y limpiamos "cart"
                onCheckout = {
                    navController.navigate("products") {
                        popUpTo("cart") { inclusive = true }
                    }
                }
            )
        }
    }
}

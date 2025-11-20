
package com.example.catalogoapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.catalogoapp.ui.screens.ProductDetailScreen
import com.example.catalogoapp.ui.screens.ProductListScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "products", modifier = modifier) {
        composable("products") {
            ProductListScreen(onOpenDetail = { id ->
                navController.navigate("detail/$id")
            })
        }
        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            ProductDetailScreen(productId = id, onBack = { navController.popBackStack() })
        }
    }
}

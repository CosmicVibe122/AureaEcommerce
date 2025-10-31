package com.aureadigitallabs.aurea.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavBackStackEntry
import com.aureadigitallabs.aurea.ui.screens.login.LoginScreen
import com.aureadigitallabs.aurea.ui.screens.home.HomeScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.CatalogScreen
import com.aureadigitallabs.aurea.ui.screens.cart.CartScreen
import com.aureadigitallabs.aurea.ui.screens.admin.AdminScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.ProductDetailScreen
import com.aureadigitallabs.aurea.ui.screens.info.AboutScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login.route
    ) {
        composable(NavRoutes.Login.route) { LoginScreen(navController) }

        composable(route = NavRoutes.Home.route + "/{role}") { backStackEntry ->
            HomeScreen(navController, backStackEntry)
        }
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                ProductDetailScreen(navController, productId)
            }
        }
        composable("cart") { CartScreen(navController) }

        composable(NavRoutes.Catalog.route) { CatalogScreen(navController) }
        composable(NavRoutes.Cart.route) { CartScreen(navController) }
        composable(NavRoutes.About.route) { AboutScreen(navController) }
        composable(NavRoutes.Admin.route) { AdminScreen(navController) }
    }
}

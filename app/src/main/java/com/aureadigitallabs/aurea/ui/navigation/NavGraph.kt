package com.aureadigitallabs.aurea.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aureadigitallabs.aurea.ui.screens.login.LoginScreen
import com.aureadigitallabs.aurea.ui.screens.home.HomeScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.CatalogScreen
import com.aureadigitallabs.aurea.ui.screens.cart.CartScreen
import com.aureadigitallabs.aurea.ui.screens.admin.AdminScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.ProductDetailScreen
import com.aureadigitallabs.aurea.ui.screens.info.AboutScreen
import com.aureadigitallabs.aurea.viewmodel.CartViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel // 1. Recibe el ViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login.route
    ) {
        composable(NavRoutes.Login.route) { LoginScreen(navController) }

        composable(route = NavRoutes.Home.route + "/{role}") { backStackEntry ->
            HomeScreen(navController, backStackEntry)
        }
        composable(
            route = "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            if (productId != null) {
                // 2. Pasa el ViewModel a ProductDetailScreen
                ProductDetailScreen(navController, productId, cartViewModel)
            }
        }

        composable(NavRoutes.Catalog.route) { CatalogScreen(navController) }

        // Ruta única para el carrito
        composable(NavRoutes.Cart.route) {
            // 3. Pasa el ViewModel a CartScreen
            CartScreen(navController, cartViewModel)
        }
        composable(NavRoutes.About.route) { AboutScreen(navController) }
        composable(NavRoutes.Admin.route) { AdminScreen(navController) }
    }
}

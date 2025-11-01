package com.aureadigitallabs.aurea.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aureadigitallabs.aurea.ui.screens.admin.AddEditProductScreen
import com.aureadigitallabs.aurea.ui.screens.admin.AdminScreen
import com.aureadigitallabs.aurea.ui.screens.cart.CartScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.CatalogScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.ProductDetailScreen
import com.aureadigitallabs.aurea.ui.screens.home.HomeScreen
import com.aureadigitallabs.aurea.ui.screens.info.AboutScreen
import com.aureadigitallabs.aurea.ui.screens.login.LoginScreen
import com.aureadigitallabs.aurea.viewmodel.CartViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login.route
    ) {

        composable(NavRoutes.Login.route) {
            LoginScreen(navController)
        }

        composable(
            route = NavRoutes.Home.route + "/{role}?showPurchaseMessage={showPurchaseMessage}",
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
                navArgument("showPurchaseMessage") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val showMessage = backStackEntry.arguments?.getBoolean("showPurchaseMessage") ?: false
            HomeScreen(navController = navController, showPurchaseMessage = showMessage)
        }
        composable(
            route = NavRoutes.Catalog.route + "?category={categoryName}",
            arguments = listOf(navArgument("categoryName") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            CatalogScreen(navController = navController, categoryName = categoryName)
        }

        composable(
            route = "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            if (productId != null) {
                ProductDetailScreen(navController, productId, cartViewModel)
            }
        }

        // --- Rutas de Carrito e Información ---
        composable(NavRoutes.Cart.route) {
            CartScreen(navController, cartViewModel)
        }

        composable(NavRoutes.About.route) {
            AboutScreen(navController)
        }

        // --- Rutas de Administración ---
        composable(NavRoutes.Admin.route) {
            AdminScreen(navController)
        }

        composable(
            route = "add_edit_product?productId={productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.IntType
                defaultValue = -1 // -1 significa que es un producto nuevo
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            AddEditProductScreen(navController = navController, productId = if (productId == -1) null else productId)
        }
    }
}

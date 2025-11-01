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
import com.aureadigitallabs.aurea.ui.screens.admin.AddEditProductScreen

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
        composable(NavRoutes.Admin.route) { AdminScreen(navController) }
        composable(route = NavRoutes.Home.route + "/{role}") {
            HomeScreen(navController)
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
        composable(
            route = "add_edit_product?productId={productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            AddEditProductScreen(navController = navController, productId = if (productId == -1) null else productId)
        }
        composable(
            route = NavRoutes.Catalog.route + "?category={categoryName}", // 1. Define la ruta con un argumento opcional
            arguments = listOf(navArgument("categoryName") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->

            val categoryName = backStackEntry.arguments?.getString("categoryName")


            CatalogScreen(navController = navController, categoryName = categoryName)
        }


        composable(NavRoutes.Cart.route) {

            CartScreen(navController, cartViewModel)
        }
        composable(NavRoutes.About.route) { AboutScreen(navController) }
        composable(NavRoutes.Admin.route) { AdminScreen(navController) }
    }
}

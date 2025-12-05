package com.aureadigitallabs.aurea.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aureadigitallabs.aurea.ui.screens.admin.AddEditProductScreen
import com.aureadigitallabs.aurea.ui.screens.admin.AdminDashboardScreen
import com.aureadigitallabs.aurea.ui.screens.admin.AdminScreen
import com.aureadigitallabs.aurea.ui.screens.cart.CartScreen
import com.aureadigitallabs.aurea.ui.screens.cart.CheckoutScreen
import com.aureadigitallabs.aurea.ui.screens.cart.OrderConfirmationScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.CatalogScreen
import com.aureadigitallabs.aurea.ui.screens.catalog.ProductDetailScreen
import com.aureadigitallabs.aurea.ui.screens.home.HomeScreen
import com.aureadigitallabs.aurea.ui.screens.info.AboutScreen
import com.aureadigitallabs.aurea.ui.screens.login.LoginScreen
import com.aureadigitallabs.aurea.ui.screens.login.RegisterScreen
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

        composable(NavRoutes.Register.route) {
            RegisterScreen(navController) // Asegúrate de importar tu RegisterScreen
        }

        composable(
            route = NavRoutes.Home.route + "/{role}?showPurchaseMessage={showPurchaseMessage}",
            arguments = listOf(
                navArgument("role") {
                    type = NavType.StringType
                    defaultValue = "user"
                },
                navArgument("showPurchaseMessage") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val showMessage = backStackEntry.arguments?.getBoolean("showPurchaseMessage") ?: false

            val role = backStackEntry.arguments?.getString("role")
            HomeScreen(navController = navController, showPurchaseMessage = showMessage, role = role)
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
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId")
            if (productId != null) {
                ProductDetailScreen(navController, productId, cartViewModel)
            }
        }

        composable(
            route = "${NavRoutes.Cart.route}?role={role}",
            arguments = listOf(navArgument("role") {
                type = NavType.StringType
                defaultValue = "user" // Esto evita el crash si no llega el dato
            })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "user"
            CartScreen(navController, cartViewModel, role)
        }


        composable(NavRoutes.About.route) {
            AboutScreen(navController)
        }

        composable(NavRoutes.Admin.route) {
            AdminDashboardScreen(navController)
        }
        composable(NavRoutes.AdminProducts.route) {
            AdminScreen(navController)
        }
        composable(
            route = "add_edit_product?productId={productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId")
            AddEditProductScreen(navController = navController, productId = if (productId == -1L) null else productId)
        }

        composable(NavRoutes.Checkout.route) {
            CheckoutScreen(navController, cartViewModel)
        }

        composable(NavRoutes.OrderConfirmation.route) {
            OrderConfirmationScreen(navController)
        }
        composable(NavRoutes.Profile.route) {
            com.aureadigitallabs.aurea.ui.screens.profile.ProfileScreen(navController)
        }
        composable(NavRoutes.ProfileData.route) {
            com.aureadigitallabs.aurea.ui.screens.profile.ProfileDataScreen(navController)
        }
        composable(NavRoutes.AdminOrders.route) {
            com.aureadigitallabs.aurea.ui.screens.admin.AdminOrdersScreen(navController)
        }
        composable(NavRoutes.ClientOrders.route) {
            com.aureadigitallabs.aurea.ui.screens.profile.ClientOrderScreen(navController)
        }
        composable(NavRoutes.AdminUsers.route) {
            com.aureadigitallabs.aurea.ui.screens.admin.AdminUsersScreen(navController)
        }
        composable(NavRoutes.PaymentMethods.route) {
            com.aureadigitallabs.aurea.ui.screens.profile.PaymentMethodsScreen(navController)
        }
        composable(NavRoutes.AdminCategories.route) {
            com.aureadigitallabs.aurea.ui.screens.admin.AdminCategoryScreen(navController)
        }
    }
}

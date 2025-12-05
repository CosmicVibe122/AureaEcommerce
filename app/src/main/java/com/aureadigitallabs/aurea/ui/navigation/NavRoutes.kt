package com.aureadigitallabs.aurea.ui.navigation

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object Register : NavRoutes("register") // Nuevo
    object Home : NavRoutes("home")
    object Catalog : NavRoutes("catalog")
    object Cart : NavRoutes("cart")
    object Checkout : NavRoutes("checkout") // Nuevo
    object OrderConfirmation : NavRoutes("order_confirmation") // Nuevo
    object Profile : NavRoutes("profile") // Nuevo
    object About : NavRoutes("about")
    object Admin : NavRoutes("admin")
    object ProfileData : NavRoutes("profile_data")
    object AdminOrders : NavRoutes("admin_orders")
    object ClientOrders : NavRoutes("client_orders")
    object AdminUsers : NavRoutes("admin_users")
    object AdminProducts : NavRoutes("admin_products")

    object PaymentMethods : NavRoutes("payment_methods")

    object AdminCategories : NavRoutes("admin_categories") // Agrega esta línea
}
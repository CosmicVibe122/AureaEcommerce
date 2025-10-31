package com.aureadigitallabs.aurea.ui.navigation

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object Home : NavRoutes("home")
    object Catalog : NavRoutes("catalog")
    object Cart : NavRoutes("cart")
    object About : NavRoutes("about")
    object Admin : NavRoutes("admin")
}

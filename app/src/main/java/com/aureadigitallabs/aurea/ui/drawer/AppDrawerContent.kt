package com.aureadigitallabs.aurea.ui.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes

@Composable
fun AppDrawerContent(
    navController: NavController,
    role: String?,
    onMenuClick: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 24.dp),
        ) {
            Text(
                text = "Menú",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 12.dp, bottom = 24.dp)
            )

            NavigationDrawerItem(
                label = { Text("Ver Catálogo") },
                selected = false,
                onClick = {
                    navController.navigate("${NavRoutes.Catalog.route}?category=")
                    onMenuClick()
                },
                icon = { Icon(Icons.Default.Store, contentDescription = "Catálogo") }
            )

            NavigationDrawerItem(
                label = { Text("Ir al Carrito") },
                selected = false,
                onClick = {
                    navController.navigate("${NavRoutes.Cart.route}?role=${role ?: "user"}")
                    onMenuClick()
                },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") }
            )

            NavigationDrawerItem(
                label = { Text("Quiénes Somos") },
                selected = false,
                onClick = {
                    navController.navigate(NavRoutes.About.route)
                    onMenuClick()
                },
                icon = { Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Quiénes Somos") }
            )


            if (role == "ADMIN") {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                NavigationDrawerItem(
                    label = { Text("Panel de Administración") },
                    selected = false,
                    onClick = {
                        navController.navigate(NavRoutes.Admin.route)
                        onMenuClick()
                    },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Divider(modifier = Modifier.padding(bottom = 12.dp))
            NavigationDrawerItem(
                label = { Text("Cerrar Sesión") },
                selected = false,
                onClick = {
                    onMenuClick()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión") }
            )
        }
    }
}
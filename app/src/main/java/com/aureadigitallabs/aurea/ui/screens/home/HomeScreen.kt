package com.aureadigitallabs.aurea.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes

@Composable
fun HomeScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val role = backStackEntry.arguments?.getString("role") ?: "CLIENT"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido a Áurea", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Rol actual: $role")
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.Catalog.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Catálogo de Productos")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.Cart.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Carrito de Compras")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.About.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Quiénes Somos")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (role == "ADMIN") {
            Button(
                onClick = { navController.navigate(NavRoutes.Admin.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Panel de Administración")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = { navController.navigate(NavRoutes.Login.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión")
        }



    }
}

package com.aureadigitallabs.aurea.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    navController: NavController,
    canNavigateBack: Boolean // Parámetro para decidir si mostrar la flecha
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            // Solo muestra el ícono si se puede navegar hacia atrás
            if (canNavigateBack) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        }
    )
}



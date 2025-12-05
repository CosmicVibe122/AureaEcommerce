package com.aureadigitallabs.aurea.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.model.User
import com.aureadigitallabs.aurea.model.UserRole
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.AdminUserViewModel

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono diferente según el rol
            Icon(
                imageVector = if (user.role == UserRole.ADMIN) Icons.Default.Shield else Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "ID: ${user.id} • Rol: ${user.role.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(navController: NavController) {
    val viewModel: AdminUserViewModel = viewModel()
    val users = viewModel.users

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Gestión de Usuarios",
                navController = navController,
                canNavigateBack = true
            )
        }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (users.isEmpty()) {
                    item { Text("No hay usuarios registrados.") }
                } else {
                    items(users) { user ->
                        UserCard(user)
                    }
                }
            }
        }
    }
}
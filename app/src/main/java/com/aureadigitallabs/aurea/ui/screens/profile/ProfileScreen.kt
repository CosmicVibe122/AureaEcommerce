package com.aureadigitallabs.aurea.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ProfileOptionItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as AureaApplication
    val sessionManager = application.sessionManager
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("Cargando...") }
    var userRole by remember { mutableStateOf("") }

    // Cargar el nombre de usuario guardado
    LaunchedEffect(Unit) {
        val name = sessionManager.getUsername().first()
        val role = sessionManager.getUserRole().first()
        username = name ?: "Usuario"
        userRole = role ?: "CLIENT"
    }

    Scaffold(
        topBar = { AppTopBar(title = "Mi Perfil", navController = navController, canNavigateBack = true) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Encabezado del Perfil ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = username, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                text = if (userRole == "ADMIN") "Administrador" else "Cliente Aurea",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Opciones (Según documento: Datos, Métodos de Pago, Preferencias) ---

            Text("Cuenta y Configuración", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))

            ProfileOptionItem(Icons.Default.Person, "Datos Personales", "Editar nombre, email y dirección", onClick = {
                navController.navigate(NavRoutes.ProfileData.route) // <--- CONECTADO
            })
            ProfileOptionItem(Icons.Default.ShoppingBag, "Mis Pedidos", "Ver historial de compras") {
                navController.navigate(NavRoutes.ClientOrders.route)
            }
            ProfileOptionItem(
                icon = Icons.Default.CreditCard,
                title = "Método de Pago Preferido",
                subtitle = "Configurar Débito o Crédito",
                onClick = {
                    navController.navigate(NavRoutes.PaymentMethods.route)
                }
            )
            ProfileOptionItem(Icons.Default.Notifications, "Notificaciones", "Alertas de ofertas y pedidos") { /* TODO: Configurar alertas */ }

            Spacer(modifier = Modifier.weight(1f))

            // --- Botón Cerrar Sesión ---
            OutlinedButton(
                onClick = {
                    scope.launch {
                        sessionManager.clearAuthToken()
                        navController.navigate(NavRoutes.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}
package com.aureadigitallabs.aurea.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import kotlinx.coroutines.flow.first

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.labelMedium)
                Text(text = value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDataScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = (context.applicationContext as AureaApplication).sessionManager

    var roleDisplay by remember { mutableStateOf("...") }
    var username by remember { mutableStateOf("Cargando...") }

    // Leemos los datos guardados en el teléfono
    LaunchedEffect(Unit) {

        username = sessionManager.getUsername().first() ?: "Desconocido"
        val role = sessionManager.getUserRole().first()
        roleDisplay = if (role == "ADMIN") "Administrador" else "Cliente"
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Datos Personales", navController = navController, canNavigateBack = true)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Información de Cuenta",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InfoRow(icon = Icons.Default.Person, label = "Nombre de Usuario", value = username)

            // Placeholder para datos futuros (ya que tu modelo User aún no tiene email)
            InfoRow(icon = Icons.Default.Badge, label = "Rol", value = roleDisplay)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Nota: Para cambiar tu contraseña o nombre de usuario, contacta al soporte o usa la versión web.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
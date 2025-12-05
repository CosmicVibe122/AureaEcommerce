package com.aureadigitallabs.aurea.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun OrderConfirmationScreen(navController: NavController) {
    // 1. Obtenemos el contexto y el scope para leer la sesión
    val context = LocalContext.current
    val application = context.applicationContext as AureaApplication
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Éxito",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("¡Pedido Confirmado!", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Tu compra se ha procesado exitosamente. Pronto recibirás los detalles.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                // 2. Leemos el rol guardado antes de navegar
                scope.launch {
                    val role = application.sessionManager.getUserRole().first() ?: "user"

                    // 3. Navegamos usando el rol correcto
                    navController.navigate("${NavRoutes.Home.route}/$role") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Inicio")
        }
    }
}
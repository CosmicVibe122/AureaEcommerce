package com.aureadigitallabs.aurea.ui.screens.profile


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import kotlinx.coroutines.launch

// Componente visual para la tarjeta de opción
@Composable
fun PaymentOptionCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as AureaApplication
    val scope = rememberCoroutineScope()

    // Estado local para la selección
    var selectedMethod by remember { mutableStateOf("Débito") }

    // 1. Cargar lo que el usuario eligió la última vez (o Débito por defecto)
    LaunchedEffect(Unit) {
        application.sessionManager.getPaymentPreference().collect { saved ->
            selectedMethod = saved
        }
    }

    Scaffold(
        topBar = { AppTopBar("Preferencia de Pago", navController, true) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Selecciona tu medio de pago favorito. Esta opción aparecerá pre-seleccionada en tus futuras compras.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Opción 1: Débito
            PaymentOptionCard(
                title = "Tarjeta de Débito",
                isSelected = selectedMethod == "Débito",
                onClick = {
                    selectedMethod = "Débito"
                    scope.launch { application.sessionManager.savePaymentPreference("Débito") }
                }
            )

            // Opción 2: Crédito
            PaymentOptionCard(
                title = "Tarjeta de Crédito",
                isSelected = selectedMethod == "Crédito",
                onClick = {
                    selectedMethod = "Crédito"
                    scope.launch { application.sessionManager.savePaymentPreference("Crédito") }
                }
            )
        }
    }
}
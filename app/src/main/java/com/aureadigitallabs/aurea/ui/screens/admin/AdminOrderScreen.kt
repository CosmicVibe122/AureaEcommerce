package com.aureadigitallabs.aurea.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.data.SessionManager
import com.aureadigitallabs.aurea.model.Order
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.AdminOrderViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderCard(order: Order, onStatusChange: (String) -> Unit) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    // Color según estado
    val statusColor = when(order.status) {
        "COMPLETED" -> Color(0xFF4CAF50) // Verde
        "CANCELLED" -> Color(0xFFF44336) // Rojo
        else -> Color(0xFFFFC107)        // Amarillo (Pending)
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Pedido #${order.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Badge(containerColor = statusColor) {
                    Text(order.status, color = Color.White, modifier = Modifier.padding(4.dp))
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Cliente: ${order.user.username}", style = MaterialTheme.typography.bodyMedium)
            Text("Total: ${currencyFormat.format(order.total)}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text("Items: ${order.items.size} productos", style = MaterialTheme.typography.bodySmall)

            // Botones de Acción (Solo si está Pendiente)
            if (order.status == "PENDING") {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = { onStatusChange("CANCELLED") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Cancel, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onStatusChange("COMPLETED") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aprobar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val viewModel: AdminOrderViewModel = viewModel(
        factory = AdminOrderViewModel.Factory(sessionManager)
    )
    val orders = viewModel.orders

    Scaffold(
        topBar = { AppTopBar("Gestión de Pedidos", navController, true) }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(16.dp)
            ) {
                if (orders.isEmpty()) {
                    item { Text("No hay pedidos registrados.") }
                } else {
                    // Mostrar primero los PENDING
                    items(orders.sortedByDescending { it.id }) { order ->
                        OrderCard(order = order) { newStatus ->
                            viewModel.updateStatus(order.id, newStatus)
                        }
                    }
                }
            }
        }
    }
}
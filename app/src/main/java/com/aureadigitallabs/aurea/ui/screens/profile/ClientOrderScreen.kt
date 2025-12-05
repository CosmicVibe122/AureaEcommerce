package com.aureadigitallabs.aurea.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.model.Order
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.ClientOrderViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ClientOrderCard(order: Order) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    val statusColor = when(order.status) {
        "COMPLETED" -> Color(0xFF4CAF50) // Verde
        "CANCELLED" -> Color(0xFFF44336) // Rojo
        else -> Color(0xFFFFC107)        // Amarillo
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Pedido #${order.id}", style = MaterialTheme.typography.titleMedium)
                Badge(containerColor = statusColor) {
                    Text(order.status, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Listar items brevemente
            order.items.forEach { item ->
                Text("- ${item.product.name} x${item.quantity}", style = MaterialTheme.typography.bodyMedium)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Total: ${currencyFormat.format(order.total)}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientOrderScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as AureaApplication
    val viewModel: ClientOrderViewModel = viewModel(
        factory = ClientOrderViewModel.Factory(application.sessionManager)
    )

    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = { AppTopBar("Mis Pedidos", navController, true) }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                if (orders.isEmpty()) {
                    item { Text("Aún no tienes pedidos realizados.") }
                } else {
                    items(orders) { order ->
                        ClientOrderCard(order)
                    }
                }
            }
        }
    }
}
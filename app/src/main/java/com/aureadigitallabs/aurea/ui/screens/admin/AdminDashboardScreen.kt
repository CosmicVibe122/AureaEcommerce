package com.aureadigitallabs.aurea.ui.screens.admin

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.data.SessionManager
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import com.aureadigitallabs.aurea.viewmodel.AdminDashboardViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
            Column {
                Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp).clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController, context: Context = LocalContext.current) {

    //Instanciamos el SessionManager
    val sessionManager = remember { SessionManager(context) }

    //Usamos la Factory para crear el ViewModel con el SessionManager

    val viewModel: AdminDashboardViewModel = viewModel(
        factory = AdminDashboardViewModel.Factory(sessionManager)
    )

    val stats = viewModel.stats
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }

    Scaffold(
        topBar = { AppTopBar("Dashboard Admin", navController, true) }
    ) { padding ->

        // 3. Manejo de estados de carga y error
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Mostrar error si existe
                    errorMessage?.let { error ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // --- Sección de Estadísticas ---
                    Text("Resumen General", style = MaterialTheme.typography.titleLarge)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard(
                            title = "Ventas Totales",
                            value = currencyFormat.format(stats.totalSales),
                            icon = Icons.Default.AttachMoney,
                            color = Color(0xFF4CAF50), // Verde
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Pedidos Pendientes",
                            value = stats.pendingOrdersCount.toString(),
                            icon = Icons.Default.ShoppingBag,
                            color = Color(0xFFFF9800), // Naranja
                            modifier = Modifier.weight(1f)
                        )
                    }

                    StatCard(
                        title = "Productos Activos",
                        value = stats.activeProductsCount.toString(),
                        icon = Icons.Default.Inventory,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // --- Sección Simulada de Notificaciones ---
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Sistema actualizado correctamente. No hay alertas críticas hoy.")
                        }
                    }

                    Divider()

                    // --- Menú de Navegación ---
                    Text("Gestión", style = MaterialTheme.typography.titleLarge)

                    MenuButton("Gestionar Productos", Icons.Default.ListAlt) {
                        navController.navigate(NavRoutes.AdminProducts.route)
                    }

                    MenuButton("Gestionar Pedidos", Icons.Default.ShoppingBag) {
                        navController.navigate(NavRoutes.AdminOrders.route)
                    }

                    MenuButton("Gestionar Usuarios", Icons.Default.Person) {
                        navController.navigate(NavRoutes.AdminUsers.route)
                    }
                    MenuButton("Gestionar Categorías", Icons.Default.Category) {
                        navController.navigate(NavRoutes.AdminCategories.route)
                    }
                }
            }
        }
    }
}
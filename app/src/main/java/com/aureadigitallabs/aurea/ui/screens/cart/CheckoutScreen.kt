package com.aureadigitallabs.aurea.ui.screens.cart

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import com.aureadigitallabs.aurea.viewmodel.CartViewModel
import com.aureadigitallabs.aurea.viewmodel.OrderState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, cartViewModel: CartViewModel) {
    val context = LocalContext.current
    val cartState by cartViewModel.cartState.collectAsState()
    val orderState by cartViewModel.orderState.collectAsState()
    val application = context.applicationContext as AureaApplication
    val scope = rememberCoroutineScope()

    // Campos del formulario
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Débito") }

    // SOLUCIÓN: Estado local para bloqueo inmediato del botón
    var isProcessing by remember { mutableStateOf(false) }

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    // Observar el estado de la orden para navegar al éxito
    LaunchedEffect(orderState) {
        if (orderState is OrderState.Success) {
            isProcessing = false // Desbloquear al terminar (aunque navegamos fuera)
            navController.navigate(NavRoutes.OrderConfirmation.route) {
                popUpTo(NavRoutes.Home.route)
            }
            cartViewModel.resetOrderState()
        } else if (orderState is OrderState.Error) {
            isProcessing = false // Importante: Desbloquear si hubo error para permitir reintentar
            Toast.makeText(context, (orderState as OrderState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        application.sessionManager.getPaymentPreference().collect { savedPreference: String ->
            if (savedPreference.isNotEmpty()) {
                paymentMethod = savedPreference
            }
        }
    }

    Scaffold(
        topBar = { AppTopBar("Finalizar Compra", navController, true) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Datos de Envío", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Resumen", style = MaterialTheme.typography.titleLarge)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total a pagar:", fontWeight = FontWeight.Bold)
                Text(currencyFormat.format(cartState.total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Método de Pago", style = MaterialTheme.typography.titleLarge)

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = (paymentMethod == "Débito"), onClick = { paymentMethod = "Débito" })
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (paymentMethod == "Débito"), onClick = { paymentMethod = "Débito" })
                    Text("Tarjeta de Débito", style = MaterialTheme.typography.bodyLarge)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = (paymentMethod == "Crédito"), onClick = { paymentMethod = "Crédito" })
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (paymentMethod == "Crédito"), onClick = { paymentMethod = "Crédito" })
                    Text("Tarjeta de Crédito", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Button(
                onClick = {
                    if (address.isNotEmpty() && city.isNotEmpty()) {
                        // 1. Bloqueo inmediato
                        isProcessing = true

                        scope.launch {
                            // 2. Operación asíncrona
                            application.sessionManager.savePaymentPreference(paymentMethod)
                            // 3. Llamada a la API
                            cartViewModel.createOrder()
                        }
                    } else {
                        Toast.makeText(context, "Por favor complete la dirección", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                // El botón se deshabilita si estamos procesando localmente O si el ViewModel está cargando
                enabled = !isProcessing && orderState !is OrderState.Loading
            ) {
                if (isProcessing || orderState is OrderState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Confirmar y Pagar")
                }
            }
        }
    }
}
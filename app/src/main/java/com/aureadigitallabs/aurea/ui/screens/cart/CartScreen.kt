package com.aureadigitallabs.aurea.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// 2. Modifica la firma para aceptar el CartViewModel
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    // 3. Obtén los items directamente del ViewModel. Ya no se necesita `remember` o `LaunchedEffect`.
    val cartItems = cartViewModel.cartItems

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Carrito de Compras",
                navController = navController,
                canNavigateBack = true
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                // Estado vacío
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío")
                }
            } else {
                // Lista de productos
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { (product, quantity) ->
                        // 4. Llama a los métodos del ViewModel en el callback
                        CartItemRow(product, quantity) { newQty ->
                            cartViewModel.updateQuantity(product.id, newQty)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))
                // 5. Calcula el total usando el ViewModel
                Text(
                    "Total: \$${cartViewModel.getTotal()}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        // 6. Llama al método para limpiar el carrito del ViewModel
                        cartViewModel.clearCart()
                    }) {
                        Text("Vaciar carrito")
                    }

                    Button(onClick = { navController.navigate("catalog") }) {
                        Text("Seguir comprando")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    product: com.aureadigitallabs.aurea.model.Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("Precio unitario: \$${product.price}")
            Text("Subtotal: \$${product.price * quantity}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { onQuantityChange(quantity - 1) }) {
                    Text("-")
                }
                Text("Cantidad: $quantity")
                Button(onClick = { onQuantityChange(quantity + 1) }) {
                    Text("+")
                }
            }
        }
    }
}

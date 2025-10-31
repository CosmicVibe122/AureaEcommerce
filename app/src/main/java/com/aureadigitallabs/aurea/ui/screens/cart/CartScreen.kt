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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val cartItems = remember { mutableStateListOf<Pair<com.aureadigitallabs.aurea.model.Product, Int>>() }

    // Sincroniza los elementos con el CartManager
    LaunchedEffect(Unit) {
        cartItems.clear()
        cartItems.addAll(CartManager.cartItems)
    }

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
                        CartItemRow(product, quantity) { newQty ->
                            if (newQty > 0) {
                                CartManager.updateQuantity(product.id, newQty)
                            } else {
                                CartManager.removeFromCart(product.id)
                            }
                            cartItems.clear()
                            cartItems.addAll(CartManager.cartItems)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    "Total: \$${CartManager.getTotal()}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        CartManager.clearCart()
                        cartItems.clear()
                    }) {
                        Text("Vaciar carrito")
                    }

                    Button(onClick = { navController.popBackStack() }) {
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

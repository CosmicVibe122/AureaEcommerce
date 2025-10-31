package com.aureadigitallabs.aurea.ui.screens.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.ui.screens.cart.CartManager

@Composable
fun ProductDetailScreen(navController: NavController, productId: Int) {
    val product = ProductRepository.getProductById(productId)

    if (product == null) {
        Text("Producto no encontrado", modifier = Modifier.padding(16.dp))
        return
    }

    ProductDetailContent(product = product, navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(product: Product, navController: NavController) {
    val cartManager = CartManager

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(product.name) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Precio: \$${product.price}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(product.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                cartManager.addToCart(product)
                navController.popBackStack()
            }) {
                Text("Agregar al carrito")
            }
        }
    }
}

package com.aureadigitallabs.aurea.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.ui.screens.cart.CartManager
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import kotlin.text.toIntOrNull;


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productId: Int) {
    val product = ProductRepository.getProductById(productId)

    Scaffold(
        topBar = {
            AppTopBar(
                title = product?.name ?: "Detalle del Producto",
                navController = navController,
                canNavigateBack = true
            )
        }
    ){ paddingValues ->
        // Si el producto no se encuentra, muestra un mensaje de error.
        if (product == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Producto no encontrado.")
            }
            return@Scaffold
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            if (product.imageRes != 0) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = stringResource(R.string.product_image_description, product.name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }


            Text(product.name, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Precio: $${product.price}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(product.description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            // Botón para añadir al carrito (si lo tienes)
            Button(
                onClick = { /* Lógica para añadir al carrito */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir al Carrito")
            }
        }
    }
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

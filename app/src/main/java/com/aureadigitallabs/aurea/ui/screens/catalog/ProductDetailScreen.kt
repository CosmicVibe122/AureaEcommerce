package com.aureadigitallabs.aurea.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import com.aureadigitallabs.aurea.viewmodel.CartViewModel
import com.aureadigitallabs.aurea.viewmodel.ProductDetailViewModel
import com.aureadigitallabs.aurea.viewmodel.ProductDetailViewModelFactory

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int,
    cartViewModel: CartViewModel
) {

    val application = LocalContext.current.applicationContext as AureaApplication


    val viewModel: ProductDetailViewModel = viewModel(
        factory = ProductDetailViewModelFactory(application.repository, productId)
    )


    val product by viewModel.product.collectAsState(initial = null)

    Scaffold(
        topBar = {
            AppTopBar(
                title = product?.name ?: "Cargando...",
                navController = navController,
                canNavigateBack = true
            )
        }
    ) { paddingValues ->

        if (product == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen del producto
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = product!!.imageRes),
                        contentDescription = product!!.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = product!!.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    text = "$${product!!.price}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = product!!.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo


                Button(
                    onClick = {
                        cartViewModel.addProduct(product!!)
                        navController.navigate(NavRoutes.Cart.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Añadir al carrito",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Añadir al Carrito", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}


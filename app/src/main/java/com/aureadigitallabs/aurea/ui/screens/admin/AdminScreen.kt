package com.aureadigitallabs.aurea.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.AdminViewModel

@Composable
fun AdminProductRow(
    product: Product,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit
) {
    val context = LocalContext.current
    val imageResId = remember(product.imageName) {
        context.resources.getIdentifier(
            product.imageName,
            "drawable",
            context.packageName
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = R.drawable.aurealogo),
                contentDescription = product.name,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("ID: ${product.id} - $${product.price}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onEdit(product) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Producto")
            }
            IconButton(onClick = { onDelete(product) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Producto", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AdminScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as AureaApplication
    val viewModel: AdminViewModel = viewModel(
        factory = AdminViewModel.Factory(application.productRepository)
    )
    val products by viewModel.allProducts.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Panel de Admin", navController = navController, canNavigateBack = true)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_edit_product") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { paddingValues ->
        if (products.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando productos...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(products) { product ->
                    AdminProductRow(
                        product = product,
                        onEdit = { selectedProduct ->
                            navController.navigate("add_edit_product?productId=${selectedProduct.id}")
                        },
                        onDelete = { viewModel.deleteProduct(product) }
                    )
                }
            }
        }
    }
}

package com.aureadigitallabs.aurea.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.ui.screens.cart.CartManager



// Asegúrate de que estas importaciones están presentes al principio de tu archivo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.aureadigitallabs.aurea.R // Importante para R.drawable...
import com.aureadigitallabs.aurea.ui.common.AppTopBar

// ... otras importaciones ...

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Centra los elementos en la fila
        ) {

            if (product.imageRes != 0) {
                Image(

                    painter = painterResource(id = product.imageRes),

                    contentDescription = stringResource(R.string.product_image_description, product.name),
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp),

                    contentScale = ContentScale.Crop
                )
            }


            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${product.price}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavController) {
    val categories = Category.values()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val products = if (selectedCategory == null)
        ProductRepository.getAllProducts()
    else
        ProductRepository.getProductsByCategory(selectedCategory!!)

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Catálogo",
                navController = navController,
                canNavigateBack = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        label = { Text(category.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(products) { product ->
                    ProductCard(product) {
                        navController.navigate("productDetail/${product.id}")
                    }
                }
            }
        }
    }
}

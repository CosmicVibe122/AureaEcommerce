package com.aureadigitallabs.aurea.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.CatalogViewModel
import java.text.NumberFormat
import java.util.Locale
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    val currencyFormat = remember {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        format.maximumFractionDigits = 0
        format
    }

    val context = LocalContext.current
    val imageModel: Any = remember(product.imageName) {
        val isUrl = product.imageName.startsWith("http", ignoreCase = true)
        if (isUrl) {
            product.imageName // Si es URL, la usamos directamente
        } else {
            // Si no es URL, intentamos resolver el ID de recurso local
            val resId = context.resources.getIdentifier(
                product.imageName,
                "drawable",
                context.packageName
            )
            // Usamos el ID si se encuentra, o la cadena original si no se encuentra
            if (resId != 0) resId else product.imageName
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageModel) // Usa la URL o el ID de recurso
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                placeholder = painterResource(id = R.drawable.aurealogo), // Imagen de carga
                error = painterResource(id = R.drawable.aurealogo), // Imagen por defecto si falla la URL o el ID no se resolvió
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currencyFormat.format(product.price),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavController, categoryName: String?) {
    val application = LocalContext.current.applicationContext as AureaApplication

    val viewModel: CatalogViewModel = viewModel(
        factory = CatalogViewModel.Factory(application.productRepository)
    )

    val allProducts by viewModel.allProducts.collectAsState(initial = emptyList())

    val categories by viewModel.categories.collectAsState(initial = emptyList())

    val initialCategory = remember(categoryName, categories) {
        if (categoryName.isNullOrEmpty()) null
        else categories.find { it.name.equals(categoryName, ignoreCase = true) }
    }

    var selectedCategory by remember { mutableStateOf(initialCategory) }

    val products = if (selectedCategory == null)
        allProducts
    else
        allProducts.filter { it.category == selectedCategory!! }

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

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Todos") }
                    )
                }
                items(categories) { category ->
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
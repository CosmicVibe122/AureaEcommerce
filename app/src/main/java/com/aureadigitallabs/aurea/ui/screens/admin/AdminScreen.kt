package com.aureadigitallabs.aurea.ui.screens.admin

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
import com.aureadigitallabs.aurea.ui.common.AppTopBar

@Composable
fun ProductForm(onSubmit: (Product, Boolean) -> Unit) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.SKATE) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("ID") }
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") }
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Category.values().forEach { cat ->
                FilterChip(
                    selected = category == cat,
                    onClick = { category = cat },
                    label = { Text(cat.name) }
                )
            }
        }

        Button(onClick = {
            val idInt = id.toIntOrNull()
            val priceDouble = price.toDoubleOrNull()
            when {
                idInt == null -> println("ID inválido")
                name.isBlank() -> println("Nombre vacío")
                description.isBlank() -> println("Descripción vacía")
                priceDouble == null || priceDouble <= 0 -> println("Precio inválido")
                else -> {
                    onSubmit(Product(idInt, name, priceDouble, description, category), true)
                    // Limpiar formulario
                    id = ""; name = ""; price = ""; description = ""
                }
            }
        }) {
            Text("Guardar Producto")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController) {
    val products = remember { mutableStateListOf<Product>() }
    LaunchedEffect(Unit) {
        products.clear()
        products.addAll(ProductRepository.getAllProducts())
    }

    var showForm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {

            AppTopBar(
                title = "Panel de Administracion",
                navController = navController,
                canNavigateBack = true // Queremos la flecha para volver
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = { showForm = !showForm }) {
                Text(if (showForm) "Cerrar Formulario" else "Agregar Producto")
            }

            if (showForm) {
                Spacer(modifier = Modifier.height(16.dp))
                ProductForm(onSubmit = { product, isNew ->
                    if (isNew) {
                        if (products.any { it.id == product.id }) {
                            // ID duplicado
                            println("Error: ID ya existe")
                        } else {
                            products.add(product)
                        }
                    } else {
                        // Editar
                        val index = products.indexOfFirst { it.id == product.id }
                        if (index != -1) products[index] = product
                    }
                })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(products) { product ->
                    AdminProductRow(product = product, onDelete = {
                        products.remove(product)
                    }, onEdit = {
                        showForm = true
                        // Aquí podrías cargar los datos en ProductForm para editar
                    })
                }
            }
        }
    }
}
@Composable
fun AdminProductRow(product: Product, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("Precio: \$${product.price}")
                Text("Categoría: ${product.category}")
            }

            Row {
                Button(onClick = onEdit, modifier = Modifier.padding(end = 8.dp)) {
                    Text("Editar")
                }
                Button(onClick = onDelete) {
                    Text("Eliminar")
                }
            }
        }
    }
}
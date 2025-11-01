package com.aureadigitallabs.aurea.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.AddEditProductViewModel
import com.aureadigitallabs.aurea.viewmodel.AddEditProductViewModelFactory
import com.aureadigitallabs.aurea.viewmodel.ProductUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(navController: NavController, productId: Int?) {
    val application = LocalContext.current.applicationContext as AureaApplication
    val viewModel: AddEditProductViewModel = viewModel(
        factory = AddEditProductViewModelFactory(application.repository, productId)
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (productId == null) "Añadir Producto" else "Editar Producto",
                navController = navController,
                canNavigateBack = true
            )
        }
    ) { paddingValues ->
        ProductForm(
            productUiState = viewModel.productUiState,
            onStateChange = viewModel::updateUiState,
            onSaveClick = {
                viewModel.saveProduct()
                navController.popBackStack() // Vuelve a la pantalla de admin
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductForm(
    productUiState: ProductUiState,
    onStateChange: (ProductUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo para el nombre
        OutlinedTextField(
            value = productUiState.name,
            onValueChange = { onStateChange(productUiState.copy(name = it)) },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campo para el precio
        OutlinedTextField(
            value = productUiState.price,
            onValueChange = { onStateChange(productUiState.copy(price = it)) },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campo para la descripción
        OutlinedTextField(
            value = productUiState.description,
            onValueChange = { onStateChange(productUiState.copy(description = it)) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        // Dropdown para la categoría
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = productUiState.category.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Category.values().forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onStateChange(productUiState.copy(category = category))
                            expanded = false
                        }
                    )
                }
            }
        }

        // Botón de Guardar
        Button(
            onClick = onSaveClick,
            enabled = productUiState.isEntryValid, // Se activa solo si los datos son válidos
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Producto")
        }
    }
}

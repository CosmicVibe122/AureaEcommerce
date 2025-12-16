package com.aureadigitallabs.aurea.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.model.Category // <--- IMPORTANTE: Asegúrate de tener este import
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.AddEditProductViewModel
import com.aureadigitallabs.aurea.viewmodel.ProductUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(navController: NavController, productId: Long?) {
    val application = LocalContext.current.applicationContext as AureaApplication

    val viewModel: AddEditProductViewModel = viewModel(
        factory = AddEditProductViewModel.Factory(application.productRepository, productId)
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (productId == null || productId == 0L) "Añadir Producto" else "Editar Producto",
                navController = navController,
                canNavigateBack = true
            )
        }
    ) { paddingValues ->
        ProductForm(
            productUiState = viewModel.productUiState,
            categories = viewModel.categoriesList, // <--- PASAMOS LA LISTA DEL VIEWMODEL
            onStateChange = viewModel::updateUiState,
            onSaveClick = {
                viewModel.saveProduct()
                navController.popBackStack()
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductForm(
    productUiState: ProductUiState,
    categories: List<Category>, // <--- RECIBIMOS LA LISTA AQUÍ
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
        OutlinedTextField(
            value = productUiState.name,
            onValueChange = { onStateChange(productUiState.copy(name = it)) },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = productUiState.price,
            onValueChange = { onStateChange(productUiState.copy(price = it)) },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = productUiState.description,
            onValueChange = { onStateChange(productUiState.copy(description = it)) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = productUiState.imageName,
            onValueChange = { onStateChange(productUiState.copy(imageName = it)) },
            label = { Text("URL de la Imagen (Opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = productUiState.category?.name ?: "Seleccione Categoría",
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // AQUÍ ITERAMOS LAS CATEGORÍAS
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category.name) },
                        onClick = {
                            // Actualizamos el estado con la categoría seleccionada
                            onStateChange(productUiState.copy(category = category))
                            expanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = onSaveClick,
            enabled = productUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Producto")
        }
    }
}
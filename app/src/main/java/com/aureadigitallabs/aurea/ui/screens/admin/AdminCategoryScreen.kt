package com.aureadigitallabs.aurea.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.ui.common.getCategoryIcon
import com.aureadigitallabs.aurea.viewmodel.AdminCategoryViewModel

@Composable
fun CategoryDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var selectedIcon by remember { mutableStateOf(category?.iconName ?: "skate") }

    val availableIcons = listOf("skate", "roller", "bmx")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "Nueva Categoría" else "Editar Categoría") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )

                Text("Selecciona un Ícono:", style = MaterialTheme.typography.bodyMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    availableIcons.forEach { iconKey ->
                        val isSelected = selectedIcon == iconKey
                        val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(bgColor, CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                .clickable { selectedIcon = iconKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(iconKey),
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotEmpty()) onConfirm(name, selectedIcon)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCategoryScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as AureaApplication

    // CORRECCIÓN: Llamada limpia a la Factory
    val viewModel: AdminCategoryViewModel = viewModel(
        factory = AdminCategoryViewModel.Factory(application.productRepository)
    )

    val categories by viewModel.categories.collectAsState()

    // Estado para controlar el diálogo
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    if (showDialog) {
        CategoryDialog(
            category = selectedCategory,
            onDismiss = { showDialog = false },
            onConfirm = { name, icon ->
                viewModel.saveCategory(selectedCategory?.id ?: 0, name, icon)
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = { AppTopBar("Gestionar Categorías", navController, true) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedCategory = null // Modo crear
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(category.iconName),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(category.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))

                            IconButton(onClick = {
                                selectedCategory = category
                                showDialog = true
                            }) { Icon(Icons.Default.Edit, "Editar") }

                            IconButton(onClick = { viewModel.deleteCategory(category.id) }) {
                                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
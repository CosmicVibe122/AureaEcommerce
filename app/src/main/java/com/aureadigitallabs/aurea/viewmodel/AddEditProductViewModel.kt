package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProductUiState(
    var id: Long = 0,
    var name: String = "",
    var price: String = "",
    var description: String = "",
    var category: Category? = null, // Puede ser nulo al inicio
    var imageName: String = "",
    val isEntryValid: Boolean = false
)

class AddEditProductViewModel(
    private val repository: ProductRepository,
    private val productId: Long?
) : ViewModel() {

    var productUiState by mutableStateOf(ProductUiState())
        private set

    // Lista para llenar el dropdown
    var categoriesList by mutableStateOf<List<Category>>(emptyList())
        private set

    init {
        // Cargar categorías y preseleccionar la primera si es un producto nuevo
        viewModelScope.launch {
            repository.getCategories().collect {
                categoriesList = it
                if (productId == null && it.isNotEmpty() && productUiState.category == null) {
                    productUiState = productUiState.copy(category = it.first())
                }
            }
        }

        if (productId != null && productId > 0) {
            viewModelScope.launch {
                repository.getProductById(productId).first()?.let { product ->
                    productUiState = product.toUiState()
                }
            }
        }
    }

    fun updateUiState(newState: ProductUiState) {
        productUiState = newState.copy(isEntryValid = validateInput(newState))
    }

    fun saveProduct() {
        if (validateInput(productUiState)) {
            viewModelScope.launch {
                val productToSave = productUiState.toProduct()
                if (productId == null || productId == 0L) {
                    repository.insert(productToSave)
                } else {
                    repository.update(productToSave)
                }
            }
        }
    }

    private fun validateInput(uiState: ProductUiState = productUiState): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && description.isNotBlank() && category != null
        }
    }

    companion object {
        fun Factory(repository: ProductRepository, productId: Long?): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddEditProductViewModel(repository, productId) as T
            }
        }
    }
}

fun ProductUiState.toProduct(): Product = Product(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    description = description,
    category = category!!,
    imageName = if (imageName.isNotBlank()) imageName else category?.iconName ?: "skate"
)

fun Product.toUiState(): ProductUiState = ProductUiState(
    id = id,
    name = name,
    price = price.toString(),
    description = description,
    category = category,
    imageName = imageName,
    isEntryValid = true
)
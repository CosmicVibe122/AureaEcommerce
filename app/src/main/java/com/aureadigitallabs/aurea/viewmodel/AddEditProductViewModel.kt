package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


data class ProductUiState(
    var id: Int = 0,
    var name: String = "",
    var price: String = "",
    var description: String = "",
    var category: Category = Category.SKATE,
    val isEntryValid: Boolean = false
)

class AddEditProductViewModel(
    private val repository: ProductRepository,
    private val productId: Int?
) : ViewModel() {


    var productUiState by mutableStateOf(ProductUiState())
        private set

    init {

        if (productId != null) {
            viewModelScope.launch {
                val product = repository.getProductById(productId).first()
                productUiState = ProductUiState(
                    id = product.id,
                    name = product.name,
                    price = product.price.toString(),
                    description = product.description,
                    category = product.category
                )
            }
        }
    }


    fun updateUiState(newState: ProductUiState) {
        productUiState = newState.copy(
            isEntryValid = validateInput(newState)
        )
    }


    fun saveProduct() {
        if (validateInput(productUiState)) {
            viewModelScope.launch {
                val productToSave = productUiState.toProduct()
                if (productId == null) {
                    repository.insert(productToSave)
                } else {
                    repository.update(productToSave)
                }
            }
        }
    }


    private fun validateInput(uiState: ProductUiState = productUiState): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && description.isNotBlank()
        }
    }
}


fun ProductUiState.toProduct(): Product = Product(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    description = description,
    category = category,
    imageRes = when (category) {
        Category.SKATE -> R.drawable.skatepro
        Category.ROLLER -> R.drawable.rollerinline
        Category.BMX -> R.drawable.bmxstreet
    }
)

// Factory para el ViewModel
class AddEditProductViewModelFactory(
    private val repository: ProductRepository,
    private val productId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditProductViewModel(repository, productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



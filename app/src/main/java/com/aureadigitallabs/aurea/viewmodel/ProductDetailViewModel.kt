package com.aureadigitallabs.aurea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.Flow

class ProductDetailViewModel(repository: ProductRepository, productId: Long) : ViewModel() {


    val product: Flow<Product?> = repository.getProductById(productId)


    companion object {
        fun Factory(repository: ProductRepository, productId: Long): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
                        return ProductDetailViewModel(repository, productId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
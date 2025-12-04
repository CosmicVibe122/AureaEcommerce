package com.aureadigitallabs.aurea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(private val repository: ProductRepository) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)


    @OptIn(ExperimentalCoroutinesApi::class)
    val allProducts = refreshTrigger.flatMapLatest {
        repository.getAllProducts()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun refresh() {
        refreshTrigger.value += 1
    }


    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
            refresh()
        }
    }

    companion object {
        fun Factory(repository: ProductRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(repository) as T
            }
        }
    }
}
package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminCategoryViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            isLoading = true
            repository.getCategories().collect {
                _categories.value = it
                isLoading = false
            }
        }
    }

    fun saveCategory(id: Long, name: String, iconName: String) {
        viewModelScope.launch {
            isLoading = true
            val newCategory = Category(id = id, name = name, iconName = iconName)
            if (id == 0L) {
                repository.createCategory(newCategory)
            } else {
                repository.updateCategory(newCategory)
            }
            loadCategories() // Recargar lista
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            isLoading = true
            repository.deleteCategory(id)
            loadCategories()
        }
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AdminCategoryViewModel(repository) as T
        }
    }
}
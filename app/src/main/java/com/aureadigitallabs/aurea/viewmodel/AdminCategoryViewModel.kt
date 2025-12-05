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
            try {
                repository.getCategories().collect {
                    _categories.value = it
                    isLoading = false
                }
            } catch (e: Exception) {
                isLoading = false
            }
        }
    }

    fun saveCategory(id: Long, name: String, iconName: String) {
        viewModelScope.launch {
            isLoading = true
            val newCategory = Category(id = id, name = name, iconName = iconName)
            try {
                if (id == 0L) {
                    repository.createCategory(newCategory)
                } else {
                    repository.updateCategory(newCategory)
                }
                loadCategories()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.deleteCategory(id)
                loadCategories()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // CORRECCIÓN: Usar companion object para la Factory
    companion object {
        fun Factory(repository: ProductRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminCategoryViewModel(repository) as T
            }
        }
    }
}
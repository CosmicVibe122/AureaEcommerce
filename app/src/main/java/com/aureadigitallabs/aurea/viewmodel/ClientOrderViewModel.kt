package com.aureadigitallabs.aurea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.data.SessionManager
import com.aureadigitallabs.aurea.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ClientOrderViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadUserOrders()
    }

    private fun loadUserOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Obtener ID del usuario actual
                val currentUserId = sessionManager.getUserId().first()

                if (currentUserId != null) {
                    // 2. Traer todos los pedidos y filtrar localmente (o usar endpoint de backend si existe)
                    val allOrders = RetrofitClient.service.getAllOrders()
                    val userOrders = allOrders.filter { it.user.id == currentUserId }

                    _orders.value = userOrders.sortedByDescending { it.id }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Factory boilerplate
    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ClientOrderViewModel(sessionManager) as T
        }
    }
}
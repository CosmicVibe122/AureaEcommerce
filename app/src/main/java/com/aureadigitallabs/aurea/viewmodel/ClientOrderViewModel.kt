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

    // Variable para errores (opcional, pero recomendada)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadUserOrders()
    }

    private fun loadUserOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // 1. Obtener ID y Token del usuario actual
                val currentUserId = sessionManager.getUserId().first()
                val token = sessionManager.getAuthToken().first()

                if (currentUserId != null && !token.isNullOrEmpty()) {
                    // 2. Traer pedidos pasando el token
                    // NOTA: Si tu backend devuelve TODOS los pedidos, el filtro local 'filter' es necesario.
                    // Si tu backend ya filtra por usuario según el token, el filtro local es redundante pero inofensivo.
                    val allOrders = RetrofitClient.service.getAllOrders("Bearer $token") //

                    val userOrders = allOrders.filter { it.user.id == currentUserId }

                    _orders.value = userOrders.sortedByDescending { it.id }
                } else {
                    _errorMessage.value = "No se encontró sesión activa."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error al cargar pedidos: ${e.message}"
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
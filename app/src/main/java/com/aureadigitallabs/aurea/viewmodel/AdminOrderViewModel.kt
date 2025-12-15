package com.aureadigitallabs.aurea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.data.SessionManager
import com.aureadigitallabs.aurea.model.Order
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// 1. Agregamos SessionManager al constructor
class AdminOrderViewModel(private val sessionManager: SessionManager) : ViewModel() {

    var orders by mutableStateOf<List<Order>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Agregamos variable para manejar errores en UI
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // 2. Obtenemos el token
                val token = sessionManager.getAuthToken().first()

                if (token.isNullOrEmpty()) {
                    errorMessage = "No has iniciado sesión."
                    return@launch
                }

                // 3. Pasamos el token a la llamada (Asegúrate de que ApiService pida el token)
                orders = RetrofitClient.service.getAllOrders("Bearer $token")

            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Error al cargar pedidos: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            try {

                val updatedOrder = RetrofitClient.service.updateOrderStatus(orderId, newStatus)

                orders = orders.map { currentOrder ->
                    if (currentOrder.id == orderId) updatedOrder else currentOrder
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Error al actualizar: ${e.localizedMessage}"
                // Recargamos por seguridad si falla
                loadOrders()
            }
        }
    }

    // 4. La Factory obligatoria para pasar el SessionManager
    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminOrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdminOrderViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
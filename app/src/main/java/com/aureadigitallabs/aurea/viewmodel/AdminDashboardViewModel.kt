package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.data.SessionManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class DashboardStats(
    val totalSales: Double = 0.0,
    val pendingOrdersCount: Int = 0,
    val activeProductsCount: Int = 0,
    val topProductName: String = "N/A" // Nuevo campo
)

class AdminDashboardViewModel(private val sessionManager: SessionManager) : ViewModel() {
    var stats by mutableStateOf(DashboardStats())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val token = sessionManager.getAuthToken().first()

                if (token.isNullOrEmpty()) {
                    errorMessage = "No hay sesión activa. Por favor inicia sesión."
                    return@launch
                }

                val authHeader = "Bearer $token"


                val orders = RetrofitClient.service.getAllOrders(authHeader)
                val products = RetrofitClient.service.getProducts()
                val completedOrders = orders.filter { it.status == "COMPLETED" }
                val totalSales = completedOrders.sumOf { it.total }
                val pendingOrders = orders.count { it.status == "PENDING" }
                val productCount = products.size

                val topProduct = completedOrders
                    .flatMap { it.items } // Obtener todos los items de todas las órdenes
                    .groupBy { it.product.id } // Agrupar por ID de producto
                    .mapValues { entry -> entry.value.sumOf { it.quantity } } // Sumar la cantidad vendida
                    .maxByOrNull { it.value } // Encontrar el que tiene la mayor cantidad

                val topProductId = topProduct?.key

                val topProductName = if (topProductId != null) {
                    // Buscar el nombre en la lista de productos
                    products.firstOrNull { it.id == topProductId }?.name ?: "Desconocido"
                } else {
                    "Sin ventas"
                }

                stats = DashboardStats(totalSales, pendingOrders, productCount, topProductName)

            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Error al cargar datos: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminDashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdminDashboardViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


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
    val activeProductsCount: Int = 0
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
                val totalSales = orders.filter { it.status == "COMPLETED" }.sumOf { it.total }
                val pendingOrders = orders.count { it.status == "PENDING" }
                val productCount = products.size

                stats = DashboardStats(totalSales, pendingOrders, productCount)

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


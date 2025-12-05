package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class DashboardStats(
    val totalSales: Double = 0.0,
    val pendingOrdersCount: Int = 0,
    val activeProductsCount: Int = 0
)

class AdminDashboardViewModel : ViewModel() {
    var stats by mutableStateOf(DashboardStats())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            isLoading = true
            try {
                val ordersDeferred = async { RetrofitClient.service.getAllOrders() }
                val productsDeferred = async { RetrofitClient.service.getProducts() }
                val orders = ordersDeferred.await()
                val products = productsDeferred.await()

                val totalSales = orders.filter { it.status == "COMPLETED" }.sumOf { it.total }
                val pendingOrders = orders.count { it.status == "PENDING" }
                val productCount = products.size

                stats = DashboardStats(totalSales, pendingOrders, productCount)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
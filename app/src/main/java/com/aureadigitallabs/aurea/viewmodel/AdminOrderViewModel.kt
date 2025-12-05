package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.model.Order
import kotlinx.coroutines.launch

class AdminOrderViewModel : ViewModel() {

    var orders by mutableStateOf<List<Order>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Cargar pedidos al iniciar
    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            isLoading = true
            try {
                // Llamamos al nuevo endpoint
                orders = RetrofitClient.service.getAllOrders()
            } catch (e: Exception) {
                e.printStackTrace() // Manejo básico de error
            } finally {
                isLoading = false
            }
        }
    }

    fun updateStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.service.updateOrderStatus(orderId, newStatus)
                loadOrders() // Recargamos la lista para ver el cambio
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
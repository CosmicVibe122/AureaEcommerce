package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.model.Order
import kotlinx.coroutines.launch

class AdminOrderViewModel : ViewModel() {

    var orders by mutableStateOf<List<Order>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            isLoading = true
            try {
                orders = RetrofitClient.service.getAllOrders()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun updateStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            try {
                // 1. Obtenemos la orden ya actualizada desde el servidor
                val updatedOrder = RetrofitClient.service.updateOrderStatus(orderId, newStatus)

                // 2. Reemplazamos exactamente esa orden en nuestra lista actual
                // Esto fuerza a la UI a mostrar los datos nuevos (precio, estado, etc.)
                orders = orders.map { currentOrder ->
                    if (currentOrder.id == orderId) updatedOrder else currentOrder
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loadOrders()
            }
        }
    }
}
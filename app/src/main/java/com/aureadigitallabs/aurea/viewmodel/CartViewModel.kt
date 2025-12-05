package com.aureadigitallabs.aurea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.data.SessionManager
import com.aureadigitallabs.aurea.model.Order
import com.aureadigitallabs.aurea.model.OrderItemRequest
import com.aureadigitallabs.aurea.model.OrderRequest
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartItem(val product: Product, val quantity: Int)


data class CartState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)

// Estado del Proceso de Orden (Para manejar la UI de carga/éxito)
sealed class OrderState {
    object Idle : OrderState() // Estado inicial (nada pasando)
    object Loading : OrderState() // Cargando (Spinner)
    data class Success(val order: Order) : OrderState() // Éxito (Confirmación)
    data class Error(val message: String) : OrderState() // Error (Mostrar mensaje)
}

class CartViewModel(private val sessionManager: SessionManager) : ViewModel() {

    // --- Estado del Carrito ---
    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    // --- Estado de la Orden ---
    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    // Añadir producto
    fun addProduct(product: Product) {
        _cartState.update { currentState ->
            val existingItem = currentState.items.find { it.product.id == product.id }
            val newItems = if (existingItem != null) {
                currentState.items.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentState.items + CartItem(product, 1)
            }
            calculateTotal(newItems)
        }
    }

    // Remover producto
    fun removeProduct(product: Product) {
        _cartState.update { currentState ->
            val existingItem = currentState.items.find { it.product.id == product.id }
            val newItems = if (existingItem != null && existingItem.quantity > 1) {
                currentState.items.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity - 1) else it
                }
            } else {
                currentState.items.filterNot { it.product.id == product.id }
            }
            calculateTotal(newItems)
        }
    }

    // Vaciar carrito
    fun clearCart() {
        _cartState.value = CartState()
    }

    // Calcular total auxiliar
    private fun calculateTotal(items: List<CartItem>): CartState {
        val newTotal = items.sumOf { it.product.price * it.quantity }
        return CartState(items, newTotal)
    }

    // --- LÓGICA DE CREAR PEDIDO (API) ---
    fun createOrder() {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading

            try {
                // 1. Obtener el ID del usuario desde SessionManager
                // (Debes haber implementado getUserId() en SessionManager)
                val userId = sessionManager.getUserId().first()

                if (userId != null) {
                    // 2. Convertir items del carrito a OrderItemRequest
                    val orderItems = _cartState.value.items.map {
                        OrderItemRequest(productId = it.product.id, quantity = it.quantity)
                    }

                    // 3. Crear el objeto request
                    val request = OrderRequest(userId = userId, items = orderItems)

                    // 4. Llamar a la API
                    val responseOrder = RetrofitClient.service.createOrder(request)

                    // 5. Éxito: Limpiar carrito y actualizar estado
                    clearCart()
                    _orderState.value = OrderState.Success(responseOrder)
                } else {
                    _orderState.value = OrderState.Error("Error: Usuario no identificado. Cierre sesión e ingrese nuevamente.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _orderState.value = OrderState.Error("Error al procesar el pedido: ${e.message}")
            }
        }
    }

    // Resetear estado (útil al salir de la pantalla de confirmación)
    fun resetOrderState() {
        _orderState.value = OrderState.Idle
    }

    // --- FACTORY (Para inyectar SessionManager) ---
    companion object {
        fun Factory(sessionManager: SessionManager): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                        return CartViewModel(sessionManager) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

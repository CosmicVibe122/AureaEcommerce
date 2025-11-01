package com.aureadigitallabs.aurea.viewmodel

import androidx.lifecycle.ViewModel
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(val product: Product, val quantity: Int)


data class CartState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)

class CartViewModel : ViewModel() {
    private val _cartState = MutableStateFlow(CartState())

    val cartState: StateFlow<CartState> = _cartState.asStateFlow()


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

            val newTotal = newItems.sumOf { it.product.price * it.quantity }
            currentState.copy(items = newItems, total = newTotal)
        }
    }


    fun removeProduct(product: Product) {
        _cartState.update { currentState ->
            val existingItem = currentState.items.find { it.product.id == product.id }
            val newItems = if (existingItem != null && existingItem.quantity > 1) {
                // Si hay más de uno, reduce la cantidad
                currentState.items.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity - 1) else it
                }
            } else {
                // Si solo hay uno, o no existe, lo elimina de la lista
                currentState.items.filterNot { it.product.id == product.id }
            }
            val newTotal = newItems.sumOf { it.product.price * it.quantity }
            currentState.copy(items = newItems, total = newTotal)
        }
    }

    fun clearCart() {
        _cartState.value = CartState()
    }
}

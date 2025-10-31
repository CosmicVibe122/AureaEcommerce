package com.aureadigitallabs.aurea.ui.screens.cart

import com.aureadigitallabs.aurea.model.Product

object CartManager {
    private val _cartItems = mutableListOf<Pair<Product, Int>>() // Producto y cantidad
    val cartItems: List<Pair<Product, Int>> get() = _cartItems

    fun addToCart(product: Product) {
        val existingIndex = _cartItems.indexOfFirst { it.first.id == product.id }
        if (existingIndex != -1) {
            val current = _cartItems[existingIndex]
            _cartItems[existingIndex] = current.copy(second = current.second + 1)
        } else {
            _cartItems.add(product to 1)
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.removeAll { it.first.id == productId }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        val index = _cartItems.indexOfFirst { it.first.id == productId }
        if (index != -1 && quantity > 0) {
            val current = _cartItems[index]
            _cartItems[index] = current.copy(second = quantity)
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getTotal(): Double {
        return _cartItems.sumOf { it.first.price * it.second }
    }
}


package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.aureadigitallabs.aurea.model.Product

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<Pair<Product, Int>>()
    val cartItems: List<Pair<Product, Int>> = _cartItems

    fun addToCart(product: Product) {
        val existingItem = _cartItems.find { it.first.id == product.id }
        if (existingItem != null) {

            val index = _cartItems.indexOf(existingItem)
            val newQuantity = existingItem.second + 1
            _cartItems[index] = existingItem.copy(second = newQuantity)
        } else {

            _cartItems.add(product to 1)
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.removeAll { it.first.id == productId }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        val existingItem = _cartItems.find { it.first.id == productId }
        if (existingItem != null) {
            val index = _cartItems.indexOf(existingItem)
            if (newQuantity > 0) {
                _cartItems[index] = existingItem.copy(second = newQuantity)
            } else {
                removeFromCart(productId)
            }
        }
    }

    fun getTotal(): Double {
        return _cartItems.sumOf { (product, quantity) -> product.price * quantity }
    }

    fun clearCart() {
        _cartItems.clear()
    }
}

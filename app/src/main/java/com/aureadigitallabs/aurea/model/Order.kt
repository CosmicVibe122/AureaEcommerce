package com.aureadigitallabs.aurea.model

data class OrderRequest(
    val userId: Long,
    val items: List<OrderItemRequest>
)
data class OrderItemRequest(
    val productId: Long,
    val quantity: Int
)

data class Order(
    val id: Long,
    val user: User,
    val total: Double,
    val status: String,
    val items: List<OrderItem>
)

data class OrderItem(
    val id: Long,
    val product: Product,
    val quantity: Int,
    val priceAtPurchase: Double
)
package com.aureadigitallabs.aurea.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val category: Category
)

enum class Category {
    SKATE,
    ROLLER,
    BMX
}

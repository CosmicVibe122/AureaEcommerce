package com.aureadigitallabs.aurea.model

import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val category: Category,
    @DrawableRes val imageRes: Int = 0
)

enum class Category {
    SKATE,
    ROLLER,
    BMX
}

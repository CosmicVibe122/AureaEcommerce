package com.aureadigitallabs.aurea.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val price: Double,
    val description: String,
    val category: Category, // Ahora usa la data class, no el Enum
    val imageName: String
)
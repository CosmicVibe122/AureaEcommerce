package com.aureadigitallabs.aurea.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.RollerSkating
import androidx.compose.material.icons.filled.Skateboarding
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val price: Double,
    val description: String,
    val category: Category,
    val imageName: String
)
enum class Category(val icon: ImageVector){
    SKATE(Icons.Default.Skateboarding),
    ROLLER(Icons.Default.RollerSkating),
    BMX(Icons.AutoMirrored.Filled.DirectionsBike)
}
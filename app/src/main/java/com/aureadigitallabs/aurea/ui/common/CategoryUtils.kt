package com.aureadigitallabs.aurea.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.RollerSkating
import androidx.compose.material.icons.filled.Skateboarding
import androidx.compose.ui.graphics.vector.ImageVector

fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "skate" -> Icons.Default.Skateboarding
        "roller" -> Icons.Default.RollerSkating
        "bmx" -> Icons.AutoMirrored.Filled.DirectionsBike
        else -> Icons.Default.Category
    }
}
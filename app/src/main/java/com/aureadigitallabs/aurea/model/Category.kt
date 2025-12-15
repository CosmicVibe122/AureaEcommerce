package com.aureadigitallabs.aurea.model


data class Category(
    val id: Long? = null,
    val name: String,
    val iconName: String // "skate", "roller", "bike"
)
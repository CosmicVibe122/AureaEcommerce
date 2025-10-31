package com.aureadigitallabs.aurea.model

data class User(
    val username: String,
    val password: String,
    val role: UserRole
)

enum class UserRole {
    ADMIN,
    CLIENT
}



package com.aureadigitallabs.aurea.model

data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val role: UserRole
)

enum class UserRole {
    ADMIN,
    CLIENT
}



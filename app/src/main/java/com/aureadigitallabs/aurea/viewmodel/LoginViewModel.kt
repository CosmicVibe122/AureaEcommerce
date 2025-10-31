package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.aureadigitallabs.aurea.model.User
import com.aureadigitallabs.aurea.model.UserRole

class LoginViewModel : ViewModel() {

    // Simulación de usuarios registrados
    private val users = listOf(
        User("admin", "admin123", UserRole.ADMIN),
        User("cliente", "cliente123", UserRole.CLIENT)
    )

    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf<String?>(null)
    var loggedUser = mutableStateOf<User?>(null)

    fun login() {
        val user = users.find { it.username == username.value && it.password == password.value }
        if (user != null) {
            loggedUser.value = user
            errorMessage.value = null
        } else {
            loggedUser.value = null
            errorMessage.value = "Usuario o contraseña incorrectos"
        }
    }

    fun logout() {
        loggedUser.value = null
        username.value = ""
        password.value = ""
    }
}



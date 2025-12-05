package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.data.AuthRepository
import com.aureadigitallabs.aurea.data.SessionManager
import kotlinx.coroutines.launch
import com.aureadigitallabs.aurea.model.LoginRequest
import com.aureadigitallabs.aurea.model.LoginResponse

class LoginViewModel(private val authRepository: AuthRepository, private val sessionManager: SessionManager) : ViewModel() {

    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf<String?>(null)
    var loginResponse = mutableStateOf<LoginResponse?>(null)

    fun login() {
        viewModelScope.launch {
            val response = authRepository.login(LoginRequest(username.value, password.value))
            if (response != null) {
                sessionManager.saveAuthToken(response.token)
                sessionManager.saveUserId(response.user.id!!)
                sessionManager.saveUsername(response.user.username)
                sessionManager.saveUserRole(response.user.role.name)
                loginResponse.value = response
                errorMessage.value = null
            } else {
                errorMessage.value = "Usuario o contraseña incorrectos"
                loginResponse.value = null
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearAuthToken()
            loginResponse.value = null
            username.value = ""
            password.value = ""
        }
    }

    class Factory(private val authRepository: AuthRepository, private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(authRepository, sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

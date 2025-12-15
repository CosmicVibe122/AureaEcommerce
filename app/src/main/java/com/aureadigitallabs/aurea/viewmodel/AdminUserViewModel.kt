package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.data.SessionManager
import com.aureadigitallabs.aurea.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// 1. Recibimos SessionManager
class AdminUserViewModel(private val sessionManager: SessionManager) : ViewModel() {

    var users by mutableStateOf<List<User>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Variable para errores (opcional pero recomendada)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // 2. Obtenemos el token
                val token = sessionManager.getAuthToken().first()

                if (!token.isNullOrEmpty()) {
                    // 3. Llamamos a la API pasando el token
                    users = RetrofitClient.service.getAllUsers("Bearer $token")
                } else {
                    errorMessage = "No hay sesión activa"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Error al cargar usuarios"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            isLoading = true
            try {
                val token = sessionManager.getAuthToken().first()
                if (token != null && user.id != null) {
                    RetrofitClient.service.updateUser(user.id, "Bearer $token", user)
                    loadUsers() // Recargamos la lista para ver los cambios
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // 4. Creamos la Factory obligatoria
    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminUserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdminUserViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
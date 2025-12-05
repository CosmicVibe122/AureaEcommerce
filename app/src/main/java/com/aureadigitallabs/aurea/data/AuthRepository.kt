package com.aureadigitallabs.aurea.data

import android.util.Log
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.model.LoginRequest
import com.aureadigitallabs.aurea.model.LoginResponse
import com.aureadigitallabs.aurea.model.User

class AuthRepository {

    // Llama a la API para hacer login
    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        return try {
            RetrofitClient.service.login(loginRequest)
        } catch (e: Exception) {

            Log.e("API_LOGIN_ERROR", "Error al intentar iniciar sesión: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }

    suspend fun register(user: User): Boolean {
        return try {
            RetrofitClient.service.registerUser(user)
            true // Registro exitoso
        } catch (e: Exception) {
            android.util.Log.e("API_REGISTER_ERROR", "Error al registrar: ${e.message}")
            false // Falló el registro
        }
    }
}
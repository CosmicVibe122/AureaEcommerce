package com.aureadigitallabs.aurea.data

import android.util.Log
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.model.LoginRequest
import com.aureadigitallabs.aurea.model.LoginResponse

class AuthRepository {

    // Llama a la API para hacer login
    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        return try {
            RetrofitClient.service.login(loginRequest)
        } catch (e: Exception) {
            //Se imprime un log en Logcat con el error
            Log.e("API_LOGIN_ERROR", "Error al intentar iniciar sesión: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }
}
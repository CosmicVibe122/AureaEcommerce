package com.aureadigitallabs.aurea.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

// Se define el DataStore para guardar las preferencias de la app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(context: Context) {

    private val dataStore = context.dataStore

    // Se define una clave para guardar el token de autenticación
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    // Función para guardar el token en el DataStore
    suspend fun saveAuthToken(token: String) {
        dataStore.edit {
            it[AUTH_TOKEN] = token
        }
    }

    //Función para leer el token desde el DataStore
    fun getAuthToken() = dataStore.data.map {
        it[AUTH_TOKEN]
    }

    //Función para limpiar el token (para el logout)
    suspend fun clearAuthToken() {
        dataStore.edit {
            it.clear()
        }
    }
}
package com.aureadigitallabs.aurea.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_ID = longPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val USER_ROLE = stringPreferencesKey("user_role")
        val PAYMENT_PREFERENCE = stringPreferencesKey("payment_preference")
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { it[AUTH_TOKEN] = token }
    }

    fun getAuthToken() = dataStore.data.map { it[AUTH_TOKEN] }


    suspend fun saveUserId(id: Long) {
        dataStore.edit { it[USER_ID] = id }
    }


    fun getUserId() = dataStore.data.map { it[USER_ID] }

    suspend fun clearAuthToken() {
        dataStore.edit {
            it.clear() // Borra token y ID
        }
    }

    suspend fun saveUsername(name: String) {
        dataStore.edit { it[USERNAME] = name }
    }

    fun getUsername() = dataStore.data.map { it[USERNAME] }

    suspend fun saveUserRole(role: String) {
        dataStore.edit { it[USER_ROLE] = role }
    }

    fun getUserRole() = dataStore.data.map { it[USER_ROLE] }

    suspend fun savePaymentPreference(type: String) {
        dataStore.edit { it[PAYMENT_PREFERENCE] = type }
    }

    // Leer preferencia
    fun getPaymentPreference() = dataStore.data.map { it[PAYMENT_PREFERENCE] ?: "Débito" } // Por defecto Débito
}
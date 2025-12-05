package com.aureadigitallabs.aurea.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.model.User
import kotlinx.coroutines.launch

class AdminUserViewModel : ViewModel() {

    var users by mutableStateOf<List<User>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            try {
                users = RetrofitClient.service.getAllUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
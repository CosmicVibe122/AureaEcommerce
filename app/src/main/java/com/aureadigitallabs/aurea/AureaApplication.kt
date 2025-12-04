package com.aureadigitallabs.aurea

import android.app.Application
import com.aureadigitallabs.aurea.data.AuthRepository
import com.aureadigitallabs.aurea.data.ProductRepository
import com.aureadigitallabs.aurea.data.SessionManager

class AureaApplication : Application() {

    // Se crea una instancia única del SessionManager para toda la app
    lateinit var sessionManager: SessionManager
        private set

    // 2. Se crean los repositorios como lazy, para que solo se instancien cuando se necesiten
    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository(sessionManager) // Le pasamos el sessionManager
    }

    override fun onCreate() {
        super.onCreate()
        // Se inicializa el SessionManager con el contexto de la aplicación
        sessionManager = SessionManager(this)
    }
}
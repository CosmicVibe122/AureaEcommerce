package com.aureadigitallabs.aurea

import android.app.Application
import com.aureadigitallabs.aurea.data.AppDatabase
import com.aureadigitallabs.aurea.data.ProductRepository

class AureaApplication : Application() {
    // Usamos 'lazy' para que la base de datos y el repo solo se creen cuando se necesiten por primera vez
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }
}


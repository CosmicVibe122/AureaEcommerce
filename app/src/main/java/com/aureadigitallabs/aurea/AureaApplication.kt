package com.aureadigitallabs.aurea

import android.app.Application
import com.aureadigitallabs.aurea.data.AppDatabase
import com.aureadigitallabs.aurea.data.ProductRepository

class AureaApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }
}


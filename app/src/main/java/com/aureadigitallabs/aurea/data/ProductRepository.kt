package com.aureadigitallabs.aurea.data

import android.util.Log
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ProductRepository(private val sessionManager: SessionManager) {


    fun getAllProducts(): Flow<List<Product>> = flow {
        try {
            val products = RetrofitClient.service.getProducts()
            emit(products)
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error al obtener productos", e)
            emit(emptyList())
        }
    }

    fun getProductById(id: Long): Flow<Product?> = flow {
        try {
            val product = RetrofitClient.service.getProductById(id)
            emit(product)
        } catch (e: Exception) {
            emit(null)
        }
    }


    suspend fun insert(product: Product) {
        val token = sessionManager.getAuthToken().first()
        if (token != null) {
            try {
                // RetrofitClient enviará el producto. La API ignorará el ID 0 y creará uno nuevo.
                RetrofitClient.service.createProduct("Bearer $token", product)
                Log.d("API_SUCCESS", "Producto creado: ${product.name}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al crear: ${e.message}")
            }
        }
    }


    suspend fun update(product: Product) {
        val token = sessionManager.getAuthToken().first()
        if (token != null) {
            try {
                RetrofitClient.service.updateProduct(product.id, "Bearer $token", product)
                Log.d("API_SUCCESS", "Producto actualizado: ${product.name}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al actualizar: ${e.message}")
            }
        }
    }


    suspend fun delete(product: Product) {
        val token = sessionManager.getAuthToken().first()
        if (token != null) {
            try {
                RetrofitClient.service.deleteProduct(product.id, "Bearer $token")
                Log.d("API_SUCCESS", "Producto eliminado: ${product.id}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al eliminar: ${e.message}")
            }
        }
    }
}
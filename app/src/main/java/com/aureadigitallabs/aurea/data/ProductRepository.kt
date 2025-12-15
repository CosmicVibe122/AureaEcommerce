package com.aureadigitallabs.aurea.data

import android.util.Log
import com.aureadigitallabs.aurea.api.RetrofitClient
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ProductRepository(private val sessionManager: SessionManager) {

    // SOLUCIÓN 1: Usamos .catch {} FUERA del flow para evitar el crash "Flow exception transparency"
    fun getAllProducts(): Flow<List<Product>> = flow {
        val products = RetrofitClient.service.getProducts()
        emit(products)
    }.catch { e ->
        Log.e("API_ERROR", "Error al obtener productos", e)
        emit(emptyList())
    }

    // SOLUCIÓN 2: Manejo seguro de nulos. Si la API falla, emitimos null sin crashear.
    fun getProductById(id: Long): Flow<Product?> = flow {
        val product = RetrofitClient.service.getProductById(id)
        emit(product)
    }.catch { e ->
        Log.e("API_ERROR", "Error al obtener producto $id: ${e.message}")
        emit(null)
    }

    suspend fun insert(product: Product) {
        val token = sessionManager.getAuthToken().first()
        if (token != null) {
            try {
                RetrofitClient.service.createProduct("Bearer $token", product)
                Log.d("API_SUCCESS", "Producto creado: ${product.name}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al crear: ${e.message}")
            }
        }
    }

    suspend fun update(product: Product) {
        // Asumimos que product.id no es nulo, o usamos ?.let si fuera necesario
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

    // Categorías con .catch seguro
    fun getCategories(): Flow<List<Category>> = flow {
        val categories = RetrofitClient.service.getCategories()
        emit(categories)
    }.catch { e ->
        Log.e("API_ERROR", "Error al obtener categorías", e)
        emit(emptyList())
    }

    suspend fun createCategory(category: Category) {
        val token = sessionManager.getAuthToken().first()
        if (token != null) {
            try {
                RetrofitClient.service.createCategory("Bearer $token", category)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Log.e("REPO", "No hay token para crear categoría")
        }
    }

    // SOLUCIÓN 3: Verificación segura de ID nulo (por si Category.id es nullable)
    suspend fun updateCategory(category: Category) {
        val token = sessionManager.getAuthToken().first()
        // Verificamos que el ID exista antes de intentar actualizar
        if (token != null && category.id != null) {
            try {
                RetrofitClient.service.updateCategory(category.id, "Bearer $token", category)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun deleteCategory(id: Long) {
        val token = sessionManager.getAuthToken().first()
        if (token != null) {
            try {
                RetrofitClient.service.deleteCategory(id, "Bearer $token")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
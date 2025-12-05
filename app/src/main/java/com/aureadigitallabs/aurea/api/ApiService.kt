package com.aureadigitallabs.aurea.api

import com.aureadigitallabs.aurea.model.User
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.LoginRequest
import com.aureadigitallabs.aurea.model.LoginResponse
import com.aureadigitallabs.aurea.model.Order
import com.aureadigitallabs.aurea.model.OrderRequest
import retrofit2.http.*

interface ApiService {
    // --- AUTH ---
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("users/register")
    suspend fun registerUser(@Body user: User): User

    // NUEVO: Obtener todos los usuarios (Admin)
    @GET("users")
    suspend fun getAllUsers(): List<User>

    // --- PRODUCTOS ---
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product

    @POST("products")
    suspend fun createProduct(@Header("Authorization") token: String, @Body product: Product): Product

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Header("Authorization") token: String, @Body product: Product): Product

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long, @Header("Authorization") token: String): retrofit2.Response<Unit>

    // NUEVO: Categorías dinámicas
    @GET("categories")
    suspend fun getCategories(): List<Category>

    // --- PEDIDOS ---
    @POST("orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Order

    @GET("orders")
    suspend fun getAllOrders(): List<Order>

    @PUT("orders/{id}/status")
    suspend fun updateOrderStatus(@Path("id") id: Long, @Query("status") status: String): Order


    @POST("categories")
    suspend fun createCategory(@Body category: Category): Category

    @PUT("categories/{id}")
    suspend fun updateCategory(@Path("id") id: Long, @Body category: Category): Category

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Long): retrofit2.Response<Unit>
}
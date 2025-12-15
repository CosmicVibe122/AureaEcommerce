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
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/users/register")
    suspend fun registerUser(@Body user: User): User

    @GET("api/users/all")
    suspend fun getAllUsers(@Header("Authorization") token: String): List<User>

    // --- PRODUCTOS ---
    @GET("api/products")
    suspend fun getProducts(): List<Product>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product?

    @POST("api/products")
    suspend fun createProduct(@Header("Authorization") token: String, @Body product: Product): Product

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Header("Authorization") token: String, @Body product: Product): Product

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long, @Header("Authorization") token: String): retrofit2.Response<Unit>

    // --- CATEGORIAS ---
    @GET("api/categories")
    suspend fun getCategories(): List<Category>

    @POST("api/categories")
    suspend fun createCategory(@Header("Authorization") token: String, @Body category: Category): Category

    @PUT("api/categories/{id}")
    suspend fun updateCategory(@Path("id") id: Long, @Header("Authorization") token: String, @Body category: Category): Category

    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Long, @Header("Authorization") token: String): retrofit2.Response<Unit>

    // --- PEDIDOS ---
    @POST("api/orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Order


    @GET("api/orders")
    suspend fun getAllOrders(@Header("Authorization") token: String): List<Order>

    @PUT("api/orders/{id}/status")
    suspend fun updateOrderStatus(@Path("id") id: Long, @Query("status") status: String): Order

    // -- USUARIOS --
    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Header("Authorization") token: String, @Body user: User): User
}
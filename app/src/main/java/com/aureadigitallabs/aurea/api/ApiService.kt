package com.aureadigitallabs.aurea.api

import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.model.LoginRequest
import com.aureadigitallabs.aurea.model.LoginResponse
import retrofit2.http.*

interface ApiService {
    // --- AUTH ---
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // --- PRODUCTOS ---
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product

    // CREAR
    @POST("products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Product

    // EDITAR (Nuevo)
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Header("Authorization") token: String,
        @Body product: Product
    ): Product

    // BORRAR (Nuevo)
    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>
}
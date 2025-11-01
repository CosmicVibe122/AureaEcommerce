package com.aureadigitallabs.aurea.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface ProductDao {

        @Query("SELECT * FROM products ORDER BY name ASC")
        fun getAllProducts(): Flow<List<Product>>

        @Query("SELECT * FROM products WHERE id = :productId")
        fun getProductById(productId: Int): Flow<Product>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(products: List<Product>)


        @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignora si el producto ya existe
        suspend fun insert(product: Product)

        @Update
        suspend fun update(product: Product)

        @Delete
        suspend fun delete(product: Product)

    }
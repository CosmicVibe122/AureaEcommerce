package com.aureadigitallabs.aurea.data

import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    companion object {
        fun getInitialProducts(): List<Product> = listOf(
            Product(1, "Tabla de Skate Completa", 15000.0, "Tabla de arce de 7 capas, ideal para principiantes.", Category.SKATE, R.drawable.skatepro),
            Product(2, "Ruedas de Skate Pro", 25000.0, "Juego de 4 ruedas de uretano de alta dureza.", Category.SKATE, R.drawable.rollerinline),
            Product(3, "Patines en Línea Ajustables", 30000.0, "Patines cómodos y ajustables para todas las edades.", Category.ROLLER, R.drawable.roller),
            Product(4, "Set de Protecciones", 49990.0, "Incluye rodilleras, coderas y muñequeras.", Category.ROLLER, R.drawable.proteccionesroller),
            Product(5, "BMX Freestyle 20", 119000.0, "Bicicleta robusta para trucos en parque y calle.", Category.BMX, R.drawable.bmxstunt),
            Product(6, "Casco BMX", 25990.0, "Casco con certificación de seguridad para múltiples deportes.", Category.BMX, R.drawable.cascoskate)
        )
    }

    fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    fun getProductById(id: Int): Flow<Product> {
        return productDao.getProductById(id)
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun update(product: Product) {
        productDao.update(product)
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun delete(product: Product) {
        productDao.delete(product)
    }
    // ------------------------------------

    suspend fun insertInitialProducts() {
        productDao.insertAll(getInitialProducts())
    }
}

package com.aureadigitallabs.aurea.data


import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.R;

object ProductRepository {

    private val products = listOf(
        Product(
            1,
            "Tabla de Skate Pro",
            59990.0,
            "Tabla profesional con ejes de aluminio y ruedas 52mm.",
            Category.SKATE,
            R.drawable.skatepro
        ),
        Product(
            2,
            "Casco Skate",
            24990.0,
            "Casco certificado para deportes extremos, ajuste ergonómico.",
            Category.SKATE,
            R.drawable.cascoskate
        ),
        Product(
            3,
            "Patines Roller X",
            79990.0,
            "Patines de velocidad con rodamientos ABEC-7.",
            Category.ROLLER,
            R.drawable.rollerinline
        ),
        Product(
            4,
            "Protecciones Roller Set",
            19990.0,
            "Juego de rodilleras, coderas y muñequeras.",
            Category.ROLLER,
            R.drawable.proteccionesroller
        ),
        Product(
            5,
            "Bicicleta BMX Street",
            159990.0,
            "BMX para trucos urbanos, cuadro de acero reforzado.",
            Category.BMX,
            R.drawable.bmxstreet
        ),
        Product(
            6,
            "Casco BMX Nitro",
            29990.0,
            "Casco resistente a impactos, diseño aerodinámico.",
            Category.BMX,
            R.drawable.cascobmx
        )
    )

    fun getAllProducts(): List<Product> = products

    fun getProductsByCategory(category: Category): List<Product> {
        return products.filter { it.category == category }
    }

    fun getProductById(id: Int): Product? {
        return products.find { it.id == id }
    }
}

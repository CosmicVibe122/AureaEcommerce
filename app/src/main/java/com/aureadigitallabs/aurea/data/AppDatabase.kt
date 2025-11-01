package com.aureadigitallabs.aurea.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aureadigitallabs.aurea.data.conventers.Converters
import com.aureadigitallabs.aurea.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.aureadigitallabs.aurea.data.ProductRepository

@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aurea_database"
                )
                    // --- AÑADE ESTE BLOQUE ---
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Inserta los datos iniciales en un hilo de fondo
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val initialProducts = ProductRepository.getInitialProducts()
                                    database.productDao().insertAll(initialProducts)
                                }
                            }
                        }
                    })
                    // --- FIN DEL BLOQUE ---
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

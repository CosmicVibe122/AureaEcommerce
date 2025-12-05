package com.aureadigitallabs.aurea.data.conventers

import androidx.room.TypeConverter
import com.aureadigitallabs.aurea.model.Category
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromCategory(category: Category): String {
        return Gson().toJson(category)
    }

    @TypeConverter
    fun toCategory(categoryString: String): Category {
        return Gson().fromJson(categoryString, Category::class.java)
    }
}
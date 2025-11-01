package com.aureadigitallabs.aurea.data.conventers


import androidx.room.TypeConverter
import com.aureadigitallabs.aurea.model.Category

class Converters {

    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(categoryName: String): Category {
        return Category.valueOf(categoryName)
    }
}

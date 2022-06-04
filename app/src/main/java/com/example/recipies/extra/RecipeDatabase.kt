package com.example.recipies.extra

import android.content.Context
import androidx.room.*

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
//@TypeConverters(IngredientsConverters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun RecipeDao(): RecipeDao

    companion object {
        @Volatile
        private var instance: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase =
            instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    RecipeDatabase::class.java,
                    "recipes"
                )
                    //.addTypeConverter(IngredientsConverters::class)
                    .fallbackToDestructiveMigration()
                    .build().also {
                    instance = it
                }
            }
    }
}


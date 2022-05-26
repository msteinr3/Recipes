package com.example.recipies.extra

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun RecipeDao() : RecipeDao

    companion object {
        @Volatile
        private var instance: RecipeDatabase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipe_database"
            ).build().also { instance = it }
        }
    }
}
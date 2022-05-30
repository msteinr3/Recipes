package com.example.recipies.extra

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

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
                ).fallbackToDestructiveMigration().build().also {
                    instance = it
                }
            }
    }
}
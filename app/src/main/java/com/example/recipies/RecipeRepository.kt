package com.example.recipies

import android.app.Application
import android.icu.text.CaseMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RecipeRepository(application: Application) {
    private val recipeDao:RecipeDao?

    init {
        val db = RecipeDatabase.getDatabase(application)
        recipeDao = db.RecipeDao()
    }

    suspend fun addRecipe(recipe: Recipe) {
        recipeDao?.addRecipe(recipe)
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao?.delete(recipe)
    }

    suspend fun update(recipe: Recipe) {
        recipeDao?.update(recipe)
    }

    fun getRecipes() = recipeDao?.getRecipes()

    fun getRecipe(title: String) = recipeDao?.getRecipe(title)
}

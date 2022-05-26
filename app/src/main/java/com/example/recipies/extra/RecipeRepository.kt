package com.example.recipies.extra

import android.app.Application

class RecipeRepository(application: Application) {
    private val recipeDao: RecipeDao?

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

    //suspend fun getID(recipe: Recipe) {
    //    recipeDao?.getID(recipe)
    // }

    fun getRecipes() = recipeDao?.getRecipes()

    fun getInternetRecipes() = recipeDao?.getInternetRecipes()

    fun getMyRecipes() = recipeDao?.getMyRecipes()

    fun getFavorites() = recipeDao?.getFavorites()

    fun getRecipe(title: String) = recipeDao?.getRecipe(title)
}

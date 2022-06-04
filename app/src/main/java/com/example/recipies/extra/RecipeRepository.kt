package com.example.recipies.extra

import android.app.Application
import com.example.recipies.utils.performFetchAndSave
import com.example.recipies.utils.performFetchingAndSaving
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val remoteDataSource: RecipeRemoteDataSource,
    private val localDataSource: RecipeDao
){

    fun getRecipes() = performFetchingAndSaving(
        {localDataSource.getRecipes()},
        {remoteDataSource.getRecipes()},
        {localDataSource.addRecipes(it.recipes)}
    )

    fun getRecipe(id : Int) = performFetchAndSave { localDataSource.getRecipe(id) }

    fun getFavoriteRecipes() = localDataSource.getFavoriteRecipes()

    suspend fun update(recipe: Recipe) {
        localDataSource.update(recipe)
    }

    suspend fun addRecipe(recipe: Recipe) {
        localDataSource.addRecipe(recipe)
    }
}


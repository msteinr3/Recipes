package com.example.recipies.extra

import android.app.Application
import com.example.recipies.utils.performFetchingAndSaving
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val remoteDataSource: RecipeRemoteDataSource,
    private val localDataSource: RecipeDao
){

    fun getRecipes() = performFetchingAndSaving(
        {localDataSource.getRecipes()},
        {remoteDataSource.getRecipes()},
        {localDataSource.addRecipes(it.results)}
    )

    fun getFavoriteRecipes() = localDataSource.getFavoriteRecipes()

    fun getRecipe(id : Int) = performFetchingAndSaving(
        {localDataSource.getRecipe(id)},
        {remoteDataSource.getRecipe(id)},
        {localDataSource.addRecipe(it)}
    )

    suspend fun addRecipe(recipe: Recipe) {
        localDataSource.addRecipe(recipe)
    }
}


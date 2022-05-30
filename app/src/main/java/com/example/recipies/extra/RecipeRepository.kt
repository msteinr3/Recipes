package com.example.recipies.extra

import android.app.Application
import com.example.recipies.utils.performFetchingAndSaving
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val  remoteDataSource: RecipeRemoteDataSource,
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


/*
    fun getInternetRecipes() = performFetchingAndSaving(
        {localDataSource.getInternetRecipes()},
        {remoteDataSource.getInternetRecipes()},
        {localDataSource.addRecipes(it.results)}
    )
    //only remote?

    fun getMyRecipes() = performFetchingAndSaving(
        {localDataSource.getMyRecipes()},
        {remoteDataSource.getMyRecipes()},
        {localDataSource.addRecipes(it.results)}
    )
    //only only local?

 */

/*
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

    //suspend fun getID(recipe: wrong) {
    //    recipeDao?.getID(recipe)
    // }

    fun getRecipes() = recipeDao?.getRecipes()

    fun getInternetRecipes() = recipeDao?.getInternetRecipes()

    fun getMyRecipes() = recipeDao?.getMyRecipes()

    fun getFavorites() = recipeDao?.getFavorites()

    fun getRecipe(title: String) = recipeDao?.getRecipe(title)
}

 */

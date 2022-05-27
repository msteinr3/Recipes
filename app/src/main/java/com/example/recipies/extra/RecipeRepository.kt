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

    //suspend fun getID(recipe: wrong) {
    //    recipeDao?.getID(recipe)
    // }

    fun getRecipes() = recipeDao?.getRecipes()

    fun getInternetRecipes() = recipeDao?.getInternetRecipes()

    fun getMyRecipes() = recipeDao?.getMyRecipes()

    fun getFavorites() = recipeDao?.getFavorites()

    fun getRecipe(title: String) = recipeDao?.getRecipe(title)
}

/*
import il.co.syntax.finalkotlinproject.data.loca_db.CharacterDao
import il.co.syntax.finalkotlinproject.data.remote_db.CharacterRemoteDataSource
import com.example.recipies.utils.performFetchingAndSaving
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val  remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterDao
){

    fun getCharacters() = performFetchingAndSaving(
        {localDataSource.getAllCharacters()},
        {remoteDataSource.getCharacters()},
        {localDataSource.insertCharacters(it.results)}
    )

    fun getCharacter(id : Int) = performFetchingAndSaving(
        {localDataSource.getCharacter(id)},
        {remoteDataSource.getCharacter(id)},
        {localDataSource.insertCharacter(it)}
    )

}
 */
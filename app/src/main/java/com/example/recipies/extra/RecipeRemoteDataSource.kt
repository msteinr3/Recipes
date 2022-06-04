package com.example.recipies.extra

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRemoteDataSource @Inject constructor(
    private val recipeService: RecipeService
) : BaseDataSource() {

    suspend fun getRecipes() = getResult { recipeService.getRandomRecipes() }

    //suspend fun getRecipe(id: Int) = getResult { recipeService.getRecipe(id) }
}
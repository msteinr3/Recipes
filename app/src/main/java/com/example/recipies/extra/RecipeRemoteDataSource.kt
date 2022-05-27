package com.example.recipies.extra

import javax.inject.Inject

class RecipeRemoteDataSource @Inject constructor(
    private val recipeService: RecipeService
) : BaseDataSource() {

    suspend fun getRecipes() = getResult { recipeService.getAllRecipes() }

    suspend fun getRecipe(id: Int) = getResult { recipeService.getRecipe(id) }
}
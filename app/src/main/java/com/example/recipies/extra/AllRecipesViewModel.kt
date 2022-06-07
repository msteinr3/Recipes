package com.example.recipies.extra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllRecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository) : ViewModel() {
    val recipes = recipeRepository.getRecipes()
    val favoriteRecipes = recipeRepository.getFavoriteRecipes()

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.addRecipe(recipe)
        }
    }

    fun deleteRecipeByTitle(title: String) {
        viewModelScope.launch {
            recipeRepository.deleteByTitle(title) //id
        }
    }
}

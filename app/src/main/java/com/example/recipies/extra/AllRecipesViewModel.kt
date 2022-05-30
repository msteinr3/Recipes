package com.example.recipies.extra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllRecipesViewModel @Inject constructor(recipeRepository: RecipeRepository, id : Int) : ViewModel() {
    var recipe = recipeRepository.getRecipe(id)
    val recipes = recipeRepository.getRecipes()
    val favoriteRecipes = recipeRepository.getRecipes()

    private var repository = recipeRepository

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.addRecipe(recipe)
        }
    }
}

/*
class AllRecipesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = RecipeRepository(application)

    fun getRecipes() = repository.getRecipes()

    fun getInternetRecipes() = repository.getInternetRecipes()

    fun getMyRecipes() = repository.getMyRecipes()

    fun getFavorites() = repository.getFavorites()

    fun getRecipe(title: String) = repository.getRecipe(title)

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.addRecipe(recipe)
        }
    }

    fun delete(recipe: Recipe) {
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }

    fun update(recipe: Recipe) {
        viewModelScope.launch {
            repository.update(recipe)
        }
    }
}

/*
@HiltViewModel
class AllCharactersViewModel @Inject constructor() : ViewModel() {
}
 */
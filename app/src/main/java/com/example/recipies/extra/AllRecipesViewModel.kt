package com.example.recipies.extra

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
package com.example.recipies.extra

import androidx.lifecycle.*
import com.example.recipies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(

    private val recipeRepository: RecipeRepository) : ViewModel() {
    private val _id = MutableLiveData<Int>()

    private val _recipe = _id.switchMap {
        recipeRepository.getRecipe(it)
    }

    val recipe : LiveData<Resource<Recipe>> = _recipe

    fun update(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.update(recipe)
        }
    }

    fun setId(id: Int) {
        _id.value = id
    }


}

/*
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

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
class SingleCharacterViewModel @Inject constructor() : ViewModel() {
}
 */
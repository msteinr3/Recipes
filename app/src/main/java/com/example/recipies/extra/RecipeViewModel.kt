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

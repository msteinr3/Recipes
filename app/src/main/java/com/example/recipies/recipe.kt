package com.example.recipies

import android.net.Uri
import java.net.URI

data class Recipe(val title: String, val photo: String?, val ingredients: String, val instructions: String, var favorite: Boolean)

object RecipeManager {

    val recipes : MutableList<Recipe> = mutableListOf()

    fun add(recipe: Recipe) {
        recipes.add(recipe)
    }

    fun remove(index:Int){
        recipes.removeAt(index)
    }
}

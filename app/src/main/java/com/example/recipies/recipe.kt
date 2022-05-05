package com.example.recipies

import android.icu.text.CaseMap
import android.net.Uri
import android.os.Parcelable
import java.net.URI

data class Recipe(
    val title: String,
    val photo: String?,
    val ingredients: String,
    val instructions: String,
    val category: String,
    var favorite: Boolean,
    var internet: Boolean
)

//things to add: video, author...

object RecipeManager {

    var recipes: MutableList<Recipe> = mutableListOf()

    fun add(recipe: Recipe) {
        recipes.add(recipe)
        recipes.sortWith(compareBy<Recipe> { it.category}.thenBy { it.title })
    }

    fun remove(index: Int) {
        recipes.removeAt(index)
    }
}

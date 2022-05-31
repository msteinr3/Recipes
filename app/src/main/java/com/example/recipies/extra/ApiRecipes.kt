package com.example.recipies.extra

data class ApiRecipes(
    val offset: Int,
    val number: Int,
    val results: List<Recipe>,
    val totalResults: Int
) {
}
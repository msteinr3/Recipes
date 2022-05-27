package com.example.recipies.extra

import android.icu.text.IDNA

data class AllRecipes(
    val info: IDNA.Info,
    val results: List<Recipe>
) {
}
package com.example.recipies.extra

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {

    @GET("random?number=2")
    suspend fun getAllRecipes() : Response<ApiRecipes>

    @GET("{id}/information")
    suspend fun getRecipe(@Path("id") id : Int) : Response<Recipe>
}

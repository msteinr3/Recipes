package com.example.recipies.extra

import com.example.recipies.extra.AllRecipes
import com.example.recipies.extra.Recipe
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {

    @GET("recipe")
    suspend fun getAllRecipes() : Response<AllRecipes>

    @GET("recipe")
    suspend fun getInternetRecipes() : Response<AllRecipes>

    @GET("recipe")
    suspend fun getMyRecipes() : Response<AllRecipes>

    @GET("recipe")
    suspend fun getFavoriteRecipes() : Response<AllRecipes>

    @GET("recipe/{id}")
    suspend fun getRecipe(@Path("id") id : Int) : Response<Recipe>
}
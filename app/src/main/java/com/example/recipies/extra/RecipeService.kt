package com.example.recipies.extra

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {

    @GET("recipe")
    suspend fun getAllRecipes() : Response<AllRecipes>

    @GET("recipe/{id}")
    suspend fun getRecipe(@Path("id") id : Int) : Response<Recipe>
}

/*
    @GET("recipe")
    suspend fun getInternetRecipes() : Response<AllRecipes>

    @GET("recipe")
    suspend fun getMyRecipes() : Response<AllRecipes>

    @GET("recipe")
    suspend fun getFavoriteRecipes() : Response<AllRecipes>
 */
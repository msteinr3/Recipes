package com.example.recipies.extra

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

const val apiKey = "apiKey=7d20e2623c9a43fb80d46016f39e2121"

interface RecipeService {

    @GET("random?apiKey=7d20e2623c9a43fb80d46016f39e2121&number=2")
    suspend fun getRandomRecipes() : Response<ApiRecipes>

    //@GET("{id}/information?")
    //suspend fun getRecipe(@Path("id") id : Int) : Response<Recipe>
}

//    @GET("random?apiKey=7d20e2623c9a43fb80d46016f39e2121&number=2")
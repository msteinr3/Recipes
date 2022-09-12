package com.example.recipies.extra

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RecipeService {

    @Headers("X-RapidAPI-Key: a88f10a2d8mshc12d3ef87ca976ap105722jsndc41a165ea6d")
    @GET("random?number=5")
    suspend fun getRandomRecipes() : Response<ApiRecipes>
}


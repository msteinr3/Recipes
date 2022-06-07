package com.example.recipies.extra

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RecipeService {

    @Headers("X-RapidAPI-Key: e3529cf2e2mshfd875deaf5d88f0p1dec52jsn45165f63480a")
    @GET("random?number=1")
    suspend fun getRandomRecipes() : Response<ApiRecipes>

}


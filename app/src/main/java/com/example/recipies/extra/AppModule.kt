package com.example.recipies.extra

import android.content.Context
import android.provider.SyncStateContract
import com.example.recipies.utils.Constants
import com.example.recipies.utils.Constants.Companion.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideLocalDataBase(@ApplicationContext context: Context): RecipeDatabase =
        RecipeDatabase.getDatabase(context)

    @Provides
    fun provideRecipeDao(database: RecipeDatabase) = database.RecipeDao()

    @Provides
    fun provideRecipeService(retrofit: Retrofit): RecipeService =
        retrofit.create(RecipeService::class.java)

    @Provides
    @Singleton
    fun provideRecipeRemoteDataSource(recipeService: RecipeService) =
        RecipeRemoteDataSource(recipeService)
}
package com.example.recipies.extra

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM `recipes` ORDER BY food ASC")
    fun getRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM `recipes` WHERE `like` = 1 ORDER BY food ASC")
    fun getFavoriteRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM `recipes` WHERE id = :id")
    fun getRecipe(id: Int): LiveData<Recipe>

    @Query("DELETE FROM `recipes` WHERE food = :title")//"DELETE FROM `recipes` WHERE id = :userId")
    suspend fun deleteByTitle(title: String) //userId: Int

    @Update
    suspend fun update(vararg recipe: Recipe)

}
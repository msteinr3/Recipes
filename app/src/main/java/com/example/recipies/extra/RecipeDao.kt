package com.example.recipies.extra

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe (recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes (recipes: List<Recipe>)

    @Query("SELECT * FROM recipes ORDER BY food ASC")
    fun getRecipes() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE `like` = 1 ORDER BY food ASC")
    fun getFavoriteRecipes() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipe(id : Int) : LiveData<Recipe>

    @Delete
    suspend fun delete(recipe: Recipe)

    @Update
    suspend fun update(vararg recipe: Recipe)


    //@Query("SELECT * FROM recipes WHERE `web` = 1 AND `like` = 0")
    //fun getInternetToDelete() : LiveData<List<wrong>>
}

//what does update do? how does it work?
//edit recipe and update instead of adding new

/*
    @Query("SELECT * FROM recipes WHERE web = 1 ORDER BY food ASC")
    fun getInternetRecipes() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE web = 0 ORDER BY food ASC")
    fun getMyRecipes() : LiveData<List<Recipe>>
 */
package com.example.recipies

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*
import java.text.FieldPosition

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe (recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Update
    suspend fun update(vararg recipe: Recipe)

    //@Query("SELECT * FROM recipes_table")
    //suspend fun getID(recipe: Recipe) : Int

    @Query("SELECT * FROM recipes_table ORDER BY food ASC")
    fun getRecipes() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE web = 1 ORDER BY food ASC")
    fun getInternetRecipes() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE web = 0 ORDER BY food ASC")
    fun getMyRecipes() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE `like` = 1 ORDER BY food ASC")
    fun getFavorites() : LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE food LIKE :title")
    fun getRecipe(title:String) : Recipe
}

//what does update do? how does it work?
//edit recipe and update instead of adding new
//currently using add instead of update
//order by multiple things, categories...

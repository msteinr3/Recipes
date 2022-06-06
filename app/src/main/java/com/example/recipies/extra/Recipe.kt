package com.example.recipies.extra

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "food")
    var title: String,
    var image: String?,
    @SerializedName("creditText")
    var category: String? = null,
    var ingredients: String? = null,
    var instructions: String,
    var vegetarian: Boolean,
    var vegan: Boolean,
    var glutenFree: Boolean,
    @ColumnInfo(name = "like")
    @SerializedName("ketogenic")
    var favorite: Boolean? = null

) {
}
package com.example.recipies.extra

import android.icu.text.CaseMap
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipes")
//@TypeConverters(IngredientsConverters::class)
data class Recipe(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "food")
    var title: String,
    var image: String?,
    @SerializedName("creditText")
    var category: String? = null,
    //var extendedIngredients : List<Ingredient>,
    var ingredients: String? = null,
    var instructions: String,
    var vegetarian: Boolean,
    var vegan: Boolean,
    var glutenFree: Boolean,
    @ColumnInfo(name = "like")
    @SerializedName("ketogenic")
    var favorite: Boolean? = null

) {
/*
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (!extendedIngredients.contentEquals(other.extendedIngredients)) return false

        return true
    }

    override fun hashCode(): Int {
        return extendedIngredients.contentHashCode()
    }

 */
}

/*
@PrimaryKey
    @SerializedName("pk")
    val id: Int,
    var title: String,
    @SerializedName("featured_image")
    var image: String,
    @SerializedName("cooking_instructions")
    var instructions: String,
    var ingredients: List<String>,

    var category: String = "Other",
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var glutenFree: Boolean = false,
    @ColumnInfo(name = "like")
    var favorite: Boolean = false
 */
package com.example.recipies.extra

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe (
    @PrimaryKey
    val id : Int,
    @ColumnInfo(name = "food")
    var title: String,
    var category: String = "Other",
    var image: String?,
    var extendedIngredients : Array<Ingredient>,
    var instructions: String,
    var vegetarian: Boolean,
    var vegan: Boolean,
    var glutenFree: Boolean,
    @ColumnInfo(name = "like")
    var favorite: Boolean = false,
) {
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
}

//things to add: video, author...
//give new recipes unique id's in add
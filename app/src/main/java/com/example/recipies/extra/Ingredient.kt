package com.example.recipies.extra

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient")
data class Ingredient(
    @PrimaryKey
    var name : String
) {
}

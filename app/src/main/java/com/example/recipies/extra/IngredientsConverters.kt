package com.example.recipies.extra

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class IngredientsConverters {

    @TypeConverter
    fun stringToIngredientsArray(ingredients: String): Array<Ingredient> {
        val separated = ingredients.split(", ", "\n")
        val ans = mutableListOf<Ingredient>()
        for (i in 0..separated.size) {
            ans.add(Ingredient(separated[i]))
        }
        return ans.toTypedArray()
    }

    @TypeConverter
    fun ingredientsArrayToString(arr: Array<Ingredient>): String {
        val builder = StringBuilder("")
        for (i in 0..arr.size) {
            builder.append("${arr[i].name} + ,\n")
        }
        return builder.toString()
    }
}
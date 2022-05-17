package com.example.recipies

import android.icu.text.CaseMap
import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

@Entity(tableName = "recipes_table")
data class Recipe(
    @ColumnInfo(name = "food")
    val title: String,
    @ColumnInfo(name = "image")
    val photo: String?,
    @ColumnInfo(name = "ingr")
    val ingredients: String,
    @ColumnInfo(name = "instr")
    val instructions: String,
    @ColumnInfo(name = "cat")
    val category: String,
    @ColumnInfo(name = "like")
    var favorite: Boolean,
    @ColumnInfo(name = "web")
    var internet: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


//things to add: video, author...
//recipes.sortWith(compareBy<Recipe> { it.category}.thenBy { it.title })

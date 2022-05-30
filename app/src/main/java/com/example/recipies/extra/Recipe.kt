package com.example.recipies.extra

import android.icu.text.CaseMap
import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

@Entity(tableName = "recipes")
data class Recipe (
    @PrimaryKey
    val id : Int,

    @ColumnInfo(name = "food")
    var title: String,

    @ColumnInfo(name = "image")
    var photo: String?,

    @ColumnInfo(name = "ingr")
    var ingredients: String,

    @ColumnInfo(name = "instr")
    var instructions: String,

    @ColumnInfo(name = "cat")
    var category: String,

    @ColumnInfo(name = "like")
    var favorite: Boolean,

    @ColumnInfo(name = "web")
    var internet: Boolean
) {
}

//things to add: video, author...
//ID?
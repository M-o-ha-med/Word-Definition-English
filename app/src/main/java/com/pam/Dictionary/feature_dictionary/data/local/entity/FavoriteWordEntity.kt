package com.pam.Dictionary.feature_dictionary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteWordTable")
data class FavoriteWordEntity (
    @PrimaryKey(autoGenerate = true) val id : Int? = null,
    val wordID : Int
)
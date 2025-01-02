package com.pam.Dictionary.feature_dictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pam.Dictionary.feature_dictionary.data.local.entity.FavoriteWordEntity
import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity

@Database(
    entities = [WordInfoEntity::class , FavoriteWordEntity::class],
    version = 7
)

@TypeConverters(Converters::class)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract val wordInfoDao: WordInfoDao
    abstract val favoriteWordDAO : FavoriteWordDAO

}
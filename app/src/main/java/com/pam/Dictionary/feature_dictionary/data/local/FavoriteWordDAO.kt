package com.pam.Dictionary.feature_dictionary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pam.Dictionary.feature_dictionary.data.local.entity.FavoriteWordEntity
import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity

@Dao
interface FavoriteWordDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoriteWord: FavoriteWordEntity)

    @Query("DELETE FROM FavoriteWordTable WHERE wordId = :wordId")
    suspend fun removeFavorite(wordId: Int)

    @Query("SELECT * FROM FavoriteWordTable INNER JOIN WordInfoTable ON WordInfoTable.id = FavoriteWordTable.wordId")
    suspend fun getFavoriteWords(): List<WordInfoEntity>


}
package com.pam.Dictionary.feature_dictionary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity
import com.pam.Dictionary.feature_dictionary.domain.model.Meaning


@Dao
interface WordInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordInfos(words: List<WordInfoEntity>)

    @Query("DELETE FROM WordInfoTable WHERE word LIKE '%' || :word || '%'")
    suspend fun deleteWordInfos(word : String)

    @Query("SELECT * FROM WordInfoTable WHERE word LIKE '%' || :word || '%'")
    suspend fun getWordInfos(word: String): List<WordInfoEntity>

    @Query("SELECT * FROM WordInfoTable")
    suspend fun getAllWordInfo(): List<WordInfoEntity>

    @Query("SELECT id FROM WordInfoTable WHERE word = :word AND meanings = :meaning")
    suspend fun getWordIDbyWordAndMeaning(word:String , meaning: List<Meaning>):Int



}
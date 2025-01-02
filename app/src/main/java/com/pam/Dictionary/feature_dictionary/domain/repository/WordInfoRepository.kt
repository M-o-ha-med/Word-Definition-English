package com.pam.Dictionary.feature_dictionary.domain.repository

import com.pam.Dictionary.core.util.Resource
import com.pam.Dictionary.feature_dictionary.domain.model.Meaning
import com.pam.Dictionary.feature_dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {

    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
    suspend fun getWordIDbyWordAndMeaning(word:String , meaning: List<Meaning>): Int
    suspend fun getAllWordInfo(): Flow<Resource<List<WordInfo>>>

}
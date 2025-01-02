package com.pam.Dictionary.feature_dictionary.domain.repository

import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity

interface FavoriteWordRepository {
    suspend fun removeFavorite(wordId: Int) : Unit

    suspend fun getFavoriteWords(): List<WordInfoEntity>
}
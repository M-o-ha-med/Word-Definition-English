package com.pam.Dictionary.feature_dictionary.data.repository

import com.pam.Dictionary.feature_dictionary.data.local.FavoriteWordDAO
import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity
import com.pam.Dictionary.feature_dictionary.domain.repository.FavoriteWordRepository

class FavoriteWordRepositoryImpl(
    private val favoriteDao : FavoriteWordDAO
) : FavoriteWordRepository{

    override suspend fun removeFavorite(wordId: Int) {
        favoriteDao.removeFavorite(wordId)
    }

    override suspend fun getFavoriteWords() : List<WordInfoEntity>{
        return favoriteDao.getFavoriteWords()
    }
}
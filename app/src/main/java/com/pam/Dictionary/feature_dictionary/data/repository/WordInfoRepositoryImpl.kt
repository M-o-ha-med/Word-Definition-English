package com.pam.Dictionary.feature_dictionary.data.repository

import com.pam.Dictionary.core.util.Resource
import com.pam.Dictionary.feature_dictionary.data.local.WordInfoDao
import com.pam.Dictionary.feature_dictionary.data.remote.DictionaryApi
import com.pam.Dictionary.feature_dictionary.domain.model.Meaning
import com.pam.Dictionary.feature_dictionary.domain.model.WordInfo
import com.pam.Dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val wordInfodao: WordInfoDao,
) : WordInfoRepository {

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        // Step 1: Check if the word exists in the local database
        val localWordInfos = wordInfodao.getWordInfos(word).map { it.toWordInfo() }
        if (localWordInfos.isNotEmpty()) {
            // Step 2: If the word exists locally, emit the local data
            emit(Resource.Success(localWordInfos))
        } else {
            // Step 3: If the word does not exist locally, fetch it from the API
            try {
                val remoteWordInfos = api.getWordInfo(word)

                // Step 4: Insert the API result into the database
                wordInfodao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })

                // Step 5: Fetch the newly added data from the database and emit it
                val newWordInfos = wordInfodao.getWordInfos(word).map { it.toWordInfo() }
                emit(Resource.Success(newWordInfos))
            } catch (e: HttpException) {
                emit(Resource.Error(message = "Failed to fetch data: ${e.message}", data = localWordInfos))
            } catch (e: IOException) {
                emit(Resource.Error(message = "Check your internet connection", data = localWordInfos))
            }
        }
    }


    override suspend fun getWordIDbyWordAndMeaning(
        word: String,
        meaning: List<Meaning>
    ): Int {
        return wordInfodao.getWordIDbyWordAndMeaning(word,meaning)
    }

    override suspend fun getAllWordInfo() : Flow<Resource<List<WordInfo>>> = flow {
        val result = wordInfodao.getAllWordInfo().map { it.toWordInfo() }
        emit(Resource.Success(result))
    }




}
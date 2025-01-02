package com.pam.Dictionary.feature_dictionary.domain.usecase

import com.pam.Dictionary.core.util.Resource
import com.pam.Dictionary.feature_dictionary.domain.model.WordInfo
import com.pam.Dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow

class getAllWordInfoUseCase(private val repository: WordInfoRepository) {
    suspend operator fun invoke(): Flow<Resource<List<WordInfo>>> {

        return repository.getAllWordInfo()
    }
}
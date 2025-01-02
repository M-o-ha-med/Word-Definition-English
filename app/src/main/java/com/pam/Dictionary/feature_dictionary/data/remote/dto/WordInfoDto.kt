package com.pam.Dictionary.feature_dictionary.data.remote.dto

import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity

data class WordInfoDto(
    val meanings: List<MeaningDto>,
    val origin: String? = null,
    val phonetic: String,
    val phonetics: List<PhoneticDto>,
    val word: String,

) {
    fun toWordInfoEntity(): WordInfoEntity {
        return WordInfoEntity(
            meanings = meanings.map { it.toMeaning() },
            origin = origin.toString(),
            phonetic = phonetic,
            word = word
        )
    }

}
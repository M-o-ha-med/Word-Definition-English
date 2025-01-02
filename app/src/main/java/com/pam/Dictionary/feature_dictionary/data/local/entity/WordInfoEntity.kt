package com.pam.Dictionary.feature_dictionary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pam.Dictionary.feature_dictionary.domain.model.Meaning
import com.pam.Dictionary.feature_dictionary.domain.model.WordInfo

@Entity(tableName = "WordInfoTable")
data class WordInfoEntity(
    @PrimaryKey val id: Int? = null,
    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String? = null,
    val word: String
) {
    fun toWordInfo(): WordInfo {
        return WordInfo(
            meanings = meanings,
            origin = origin,
            phonetic = phonetic ?: "None",
            word = word
        )
    }
}
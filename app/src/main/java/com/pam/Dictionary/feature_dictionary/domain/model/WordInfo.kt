package com.pam.Dictionary.feature_dictionary.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordInfo(

    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String,
    val word: String
) : Parcelable

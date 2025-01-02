package com.pam.Dictionary.feature_dictionary.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
):Parcelable

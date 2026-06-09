package com.example.readwatch.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedBook(
    val title: String = "",
    val author: String = "",
    val thumbnail: String = "",
    val rating: Float = 0f
) : Parcelable
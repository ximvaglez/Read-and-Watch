package com.example.readwatch.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedMovie(
    val title: String = "",
    val year: String = "",
    val poster: String = "",
    val imdbID: String = "",
    val rating: Float = 0f
) : Parcelable
package com.example.readwatch.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MovieResponse(
    val Search: List<MovieItem>?,  // lista de películas
    val Response: String?          // "True" o "False"
)

@Parcelize
data class MovieItem(
    val Title: String?,
    val Year: String?,
    val Poster: String?,
    val imdbID: String?,
    val Type: String?
) : Parcelable
data class MovieDetail(
    val Title: String?,
    val Year: String?,
    val Rated: String?,
    val Runtime: String?,
    val Genre: String?,
    val Director: String?,
    val Actors: String?,
    val Plot: String?,
    val Poster: String?,
    val imdbRating: String?,
    val Response: String?
)

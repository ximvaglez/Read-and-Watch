package com.example.readwatch.core.model.network

import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.MovieDetail
import com.example.readwatch.core.model.MovieItem

interface MovieService {
    suspend fun searchMovies(
        query: String,
        page: Int
    ): ResponseService<List<MovieItem>>

    suspend fun getMovieDetail(
        imdbID: String
    ): ResponseService<MovieDetail>
}

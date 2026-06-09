package com.example.readwatch.core.model.network

import com.example.readwatch.core.model.MovieDetail
import com.example.readwatch.core.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET(".")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("type") type: String = "movie"
    ): Response<MovieResponse>

    @GET(".")
    suspend fun getMovieDetail(
        @Query("i") imdbID: String,
        @Query("apikey") apiKey: String
    ): Response<MovieDetail>
}
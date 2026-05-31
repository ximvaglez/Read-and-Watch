package com.example.readwatch.core.model.network

import com.example.readwatch.core.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface BookAPI {

    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("startIndex") startIndex: Int,
        @Query("key") apiKey: String
    ): Response<BookResponse>
}

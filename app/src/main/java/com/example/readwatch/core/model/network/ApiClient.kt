package com.example.readwatch.core.model.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor


object ApiClient {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"
        const val API_KEY = "AIzaSyDCgILSJA1BWZousPt4VnNgmpcJtul3q_g"

        private val logging = HttpLoggingInterceptor().apply{
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val bookApi: BookAPI by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookAPI::class.java)
        }
}
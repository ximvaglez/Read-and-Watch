package com.example.readwatch.core.model.network

import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.BookItem

interface BookService {
    suspend fun  getBooks(
        query: String,
        maxResults: Int = 20,
        startIndex: Int = 0
    ): ResponseService<List<BookItem>>
}
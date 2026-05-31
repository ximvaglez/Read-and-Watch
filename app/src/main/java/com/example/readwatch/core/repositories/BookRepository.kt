package com.example.readwatch.core.repositories

import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.BookItem
import com.example.readwatch.core.model.network.ApiClient
import com.example.readwatch.core.model.network.BookService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository : BookService {

    private val api = ApiClient.bookApi

    override suspend fun getBooks(
        query: String,
        maxResults: Int,
        startIndex: Int
    ): ResponseService<List<BookItem>> =

        withContext(Dispatchers.IO) {

            try {

                val response =
                    api.getBooks(
                        query = query,
                        maxResults = maxResults,
                        startIndex = startIndex,
                        apiKey = ApiClient.API_KEY
                    )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        ResponseService.Success(
                            (body.items ?: emptyList()).filter {
                                // quita los libros sin título
                                !it.volumeInfo.title.isNullOrBlank()
                            })

                    } else {

                        ResponseService.Error(
                            "Respuesta vacía del servidor"
                        )
                    }
                } else {

                    ResponseService.Error(
                        "Error ${response.code()}: ${response.message()}"
                    )
                }

            } catch (e: Exception) {

                ResponseService.Error(
                    "No se pudieron cargar los libros: ${e.localizedMessage}"
                )
            }
        }
}
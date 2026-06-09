package com.example.readwatch.core.repositories

import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.MovieDetail
import com.example.readwatch.core.model.MovieItem
import com.example.readwatch.core.model.network.ApiClient
import com.example.readwatch.core.model.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository : MovieService {

    private val api = ApiClient.movieApi

    override suspend fun searchMovies(
        query: String,
        page: Int
    ): ResponseService<List<MovieItem>> =

        withContext(Dispatchers.IO) {
            try {
                val response = api.searchMovies(
                    query = query,
                    apiKey = ApiClient.OMDB_API_KEY,
                    page = page
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.Response == "True") {
                        ResponseService.Success(
                            (body.Search ?: emptyList()).filter {
                                !it.Title.isNullOrBlank()
                            }
                        )
                    } else {
                        ResponseService.Error("No se encontraron películas")
                    }
                } else {
                    ResponseService.Error("Error ${response.code()}")
                }
            } catch (e: Exception) {
                ResponseService.Error("No se pudieron cargar las películas")
            }
        }

    override suspend fun getMovieDetail(
        imdbID: String
    ): ResponseService<MovieDetail> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getMovieDetail(
                    imdbID = imdbID,
                    apiKey = ApiClient.OMDB_API_KEY
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.Response == "True") {
                        ResponseService.Success(body)
                    } else {
                        ResponseService.Error("Película no encontrada")
                    }
                } else {
                    ResponseService.Error("Error ${response.code()}")
                }
            } catch (e: Exception) {
                ResponseService.Error("No se pudo cargar el detalle")
            }
        }
}
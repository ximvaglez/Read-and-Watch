package com.example.readwatch.home.movieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.MovieDetail
import com.example.readwatch.core.model.network.MovieService
import com.example.readwatch.core.repositories.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val service: MovieService = MovieRepository()
) : ViewModel() {

    private val _detailState =
        MutableStateFlow<ResponseService<MovieDetail>?>(null)

    val detailState: StateFlow<ResponseService<MovieDetail>?> =
        _detailState.asStateFlow()

    fun loadDetail(imdbID: String) {
        viewModelScope.launch {
            _detailState.value = ResponseService.Loading
            _detailState.value = service.getMovieDetail(imdbID)
        }
    }
}
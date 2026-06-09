package com.example.readwatch.home.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.MovieItem
import com.example.readwatch.core.model.SavedMovie
import com.example.readwatch.core.model.network.MovieService
import com.example.readwatch.core.repositories.MovieRepository
import com.example.readwatch.core.repositories.UserRepository
import com.example.readwatch.core.repositories.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val service: MovieService = MovieRepository(),
    private val userService: UserService = UserRepository()
) : ViewModel() {

    private val _movieState =
        MutableStateFlow<ResponseService<List<MovieItem>>?>(null)

    val movieState: StateFlow<ResponseService<List<MovieItem>>?> =
        _movieState.asStateFlow()

    private val _libraryState =
        MutableStateFlow<ResponseService<List<SavedMovie>>?>(null)
    val libraryState: StateFlow<ResponseService<List<SavedMovie>>?> =
        _libraryState.asStateFlow()

    fun searchMovies(query: String, page: Int = 1) {
        viewModelScope.launch {
            _movieState.value = ResponseService.Loading
            _movieState.value = service.searchMovies(query, page)
        }
    }
    fun loadLibrary() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            _libraryState.value = userService.getFavoriteMovies(uid)
        }
    }

}
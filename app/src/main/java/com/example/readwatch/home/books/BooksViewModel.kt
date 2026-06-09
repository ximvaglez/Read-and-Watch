package com.example.readwatch.home.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.BookItem
import com.example.readwatch.core.model.SavedBook
import com.example.readwatch.core.model.network.BookService
import com.example.readwatch.core.repositories.BookRepository
import com.example.readwatch.core.repositories.UserRepository
import com.example.readwatch.core.repositories.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksViewModel(
    private val service: BookService = BookRepository(),
    private val userService: UserService = UserRepository()
) : ViewModel() {

    private val _bookState =
        MutableStateFlow<ResponseService<List<BookItem>>?>(null)

    val bookState: StateFlow<ResponseService<List<BookItem>>?> =
        _bookState.asStateFlow()

    private val _libraryState = MutableStateFlow<ResponseService<List<SavedBook>>?>(null)
    val libraryState: StateFlow<ResponseService<List<SavedBook>>?> = _libraryState.asStateFlow()
    private val pageSize = 20

    fun loadBooks(
        query: String,
        page: Int = 0
    ) {

        viewModelScope.launch {

            _bookState.value = ResponseService.Loading

            _bookState.value =
                service.getBooks(query, 20, page *20)

        }
    }
    fun loadLibrary() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            _libraryState.value = userService.getFavoriteBooks(uid)
        }
    }

}
package com.example.readwatch.home.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.BookItem
import com.example.readwatch.core.model.network.BookService
import com.example.readwatch.core.repositories.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksViewModel(
    private val service: BookService = BookRepository()
) : ViewModel() {

    private val _bookState =
        MutableStateFlow<ResponseService<List<BookItem>>?>(null)

    val bookState: StateFlow<ResponseService<List<BookItem>>?> =
        _bookState.asStateFlow()

    private val pageSize = 20

    fun loadBooks(
        query: String,
        page: Int = 0
    ) {

        viewModelScope.launch {

            _bookState.value = ResponseService.Loading

            _bookState.value =
                service.getBooks(
                    query = query,
                    maxResults = pageSize,
                    startIndex = page * pageSize
                )
        }
    }
}
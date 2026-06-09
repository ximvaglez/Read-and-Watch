package com.example.readwatch.core.repositories

import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.BookItem
import com.example.readwatch.core.model.MovieItem
import com.example.readwatch.core.model.SavedBook
import com.example.readwatch.core.model.SavedMovie
import com.example.readwatch.onboarding.personalInfo.model.UserProfile

interface UserService {
    suspend fun saveUserInfo(userProfile: UserProfile): ResponseService<Unit>
    suspend fun getUserProfile(uid: String): ResponseService<UserProfile>

    suspend fun saveFavoriteBook(uid: String, book: BookItem): ResponseService<Unit>
    suspend fun saveFavoriteMovie(uid: String, movie: MovieItem): ResponseService<Unit>

    suspend fun getFavoriteBooks(uid: String): ResponseService<List<SavedBook>>

    suspend fun rateBook(uid: String, bookTitle: String, rating: Float): ResponseService<Unit>

    suspend fun getFavoriteMovies(uid: String): ResponseService<List<SavedMovie>>

    suspend fun rateMovie(uid: String, imdbID: String, rating: Float): ResponseService<Unit>

}


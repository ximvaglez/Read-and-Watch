package com.example.readwatch.core.repositories

import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.BookItem
import com.example.readwatch.core.model.MovieItem
import com.example.readwatch.core.model.SavedBook
import com.example.readwatch.core.model.SavedMovie
import com.example.readwatch.onboarding.personalInfo.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository: UserService {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("users")

    override suspend fun saveUserInfo(userProfile: UserProfile): ResponseService<Unit> = withContext(Dispatchers.IO){
        try {
            userCollection.document(userProfile.id)
                .set(userProfile)
                .await()
            ResponseService.Success(Unit)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo crear el perfil: ${e.localizedMessage}")
        }
    }
    override suspend fun getUserProfile(
        uid: String
    ): ResponseService<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val document = userCollection.document(uid).get().await()
            val profile = document.toObject(UserProfile::class.java)
            if (profile != null) {
                ResponseService.Success(profile)
            } else {
                ResponseService.Error("Perfil no encontrado")
            }
        } catch (e: Exception) {
            ResponseService.Error("No se pudo cargar el perfil")
        }
    }
    override suspend fun saveFavoriteBook(
        uid: String,
        book: BookItem
    ): ResponseService<Unit> = withContext(Dispatchers.IO) {
        try {
            val bookMap = mapOf(
                "title"     to (book.volumeInfo.title ?: ""),
                "author"    to (book.volumeInfo.authors?.joinToString(", ") ?: ""),
                "thumbnail" to (book.volumeInfo.imageLinks?.thumbnail ?: "")
            )
            firestore.collection("users")
                .document(uid)
                .collection("favoriteBooks")
                .document(book.volumeInfo.title ?: uid)
                .set(bookMap)
                .await()
            ResponseService.Success(Unit)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo guardar el libro")
        }
    }

    override suspend fun saveFavoriteMovie(
        uid: String,
        movie: MovieItem
    ): ResponseService<Unit> = withContext(Dispatchers.IO) {
        try {
            val movieMap = mapOf(
                "title"  to (movie.Title ?: ""),
                "year"   to (movie.Year ?: ""),
                "poster" to (movie.Poster ?: ""),
                "imdbID" to (movie.imdbID ?: "")
            )
            firestore.collection("users")
                .document(uid)
                .collection("favoriteMovies")
                .document(movie.imdbID ?: uid)
                .set(movieMap)
                .await()
            ResponseService.Success(Unit)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo guardar la película")
        }
    }
    override suspend fun getFavoriteBooks(
        uid: String
    ): ResponseService<List<SavedBook>> = withContext(Dispatchers.IO) {
        try {
            val docs = firestore.collection("users")
                .document(uid)
                .collection("favoriteBooks")
                .get()
                .await()
            val books = docs.map { doc ->
                SavedBook(
                    title     = doc.getString("title") ?: "",
                    author    = doc.getString("author") ?: "",
                    thumbnail = doc.getString("thumbnail") ?: "",
                    rating    = (doc.getDouble("rating") ?: 0.0).toFloat()
                )
            }
            ResponseService.Success(books)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo cargar la biblioteca")
        }
    }

    override suspend fun rateBook(
        uid: String,
        bookTitle: String,
        rating: Float
    ): ResponseService<Unit> = withContext(Dispatchers.IO) {
        try {
            firestore.collection("users")
                .document(uid)
                .collection("favoriteBooks")
                .document(bookTitle)
                .update("rating", rating)
                .await()
            ResponseService.Success(Unit)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo guardar la calificación")
        }
    }
    override suspend fun getFavoriteMovies(
        uid: String
    ): ResponseService<List<SavedMovie>> = withContext(Dispatchers.IO) {
        try {
            val docs = firestore.collection("users")
                .document(uid)
                .collection("favoriteMovies")
                .get()
                .await()
            val movies = docs.map { doc ->
                SavedMovie(
                    title   = doc.getString("title") ?: "",
                    year    = doc.getString("year") ?: "",
                    poster  = doc.getString("poster") ?: "",
                    imdbID  = doc.getString("imdbID") ?: "",
                    rating  = (doc.getDouble("rating") ?: 0.0).toFloat()
                )
            }
            ResponseService.Success(movies)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo cargar las películas guardadas")
        }
    }

    override suspend fun rateMovie(
        uid: String,
        imdbID: String,
        rating: Float
    ): ResponseService<Unit> = withContext(Dispatchers.IO) {
        try {
            firestore.collection("users")
                .document(uid)
                .collection("favoriteMovies")
                .document(imdbID)
                .update("rating", rating)
                .await()
            ResponseService.Success(Unit)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo guardar la calificación")
        }
    }
}

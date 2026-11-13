package com.binh.cinetrack.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularPaged(): Flow<PagingData<Movie>>
    fun getTrendingMovies(): Flow<List<Movie>>
    suspend fun refreshTrending()
    suspend fun getMovies(): List<Movie> // Trending
    suspend fun getPopular(page: Int = 1): List<Movie>// bị thừa ra ở hiện tại
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieDetail(id: Int): Movie?
    suspend fun getGenres(): List<Genre>
    fun getFavorites(): Flow<List<Movie>>
    fun isFavorite(movieId: Int): Flow<Boolean>
    suspend fun toggleFavorite(movie: Movie)
    suspend fun getMovieCredits(id: Int): List<Cast>
    suspend fun getSimilarMovies(id: Int): List<Movie>
    fun getMoviesByGenrePaged(genreId: Int): Flow<PagingData<Movie>>
}

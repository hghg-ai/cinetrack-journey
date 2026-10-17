package com.binh.cinetrack.domain

import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<List<Movie>>
    suspend fun refreshTrending()
    suspend fun getMovies(): List<Movie> // Trending
    suspend fun getPopular(page: Int = 1): List<Movie>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieDetail(id: Int): Movie?
    suspend fun getGenres(): List<Genre>

    fun getFavorites(): Flow<List<Movie>>
    fun isFavorite(movieId: Int): Flow<Boolean>
    suspend fun toggleFavorite(movie: Movie)
}

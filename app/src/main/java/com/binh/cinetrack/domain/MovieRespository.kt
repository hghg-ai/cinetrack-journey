package com.binh.cinetrack.domain

interface MovieRepository {
    suspend fun getMovies(): List<Movie> // Trending
    suspend fun getPopular(page: Int = 1): List<Movie>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieDetail(id: Int): Movie?
    suspend fun getGenres(): List<Genre>
}

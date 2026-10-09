package com.binh.cinetrack.data

import com.binh.cinetrack.BuildConfig
import com.binh.cinetrack.data.mapper.toMovie
import com.binh.cinetrack.data.remote.dto.RetrofitClient
import com.binh.cinetrack.data.remote.dto.TmdbApiService
import com.binh.cinetrack.domain.Genre
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository

class RemoteMovieRepository(
    private val apiService: TmdbApiService = RetrofitClient.apiService,
    private val apiKey: String = BuildConfig.TMDB_API_KEY
) : MovieRepository {

    override suspend fun getMovies(): List<Movie> {
        val response = apiService.getTrending(apiKey)
        return response.results.map { it.toMovie() }
    }

    override suspend fun getPopular(page: Int): List<Movie> {
        val response = apiService.getPopular(apiKey, page)
        return response.results.map { it.toMovie() }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        val response = apiService.searchMovies(apiKey, query)
        return response.results.map { it.toMovie() }
    }

    override suspend fun getMovieDetail(id: Int): Movie? {
        return try {
            val response = apiService.getMovieDetail(id, apiKey)
            response.toMovie()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getGenres(): List<Genre> {
        return try {
            val response = apiService.getGenres(apiKey)
            response.genres.map { Genre(it.id, it.name) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

package com.binh.cinetrack.data

import com.binh.cinetrack.BuildConfig
import com.binh.cinetrack.data.local.FavoriteDao
import com.binh.cinetrack.data.local.MovieCacheDao
import com.binh.cinetrack.data.mapper.toCachedEntity
import com.binh.cinetrack.data.mapper.toEntity
import com.binh.cinetrack.data.mapper.toMovie
import com.binh.cinetrack.data.remote.dto.RetrofitClient
import com.binh.cinetrack.data.remote.dto.TmdbApiService
import com.binh.cinetrack.domain.Genre
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.binh.cinetrack.data.mapper.toDomain
import kotlinx.coroutines.flow.first


class RemoteMovieRepository(
    private val favoriteDao: FavoriteDao,
    private val movieCacheDao: MovieCacheDao,
    private val apiService: TmdbApiService = RetrofitClient.apiService,
    private val apiKey: String = BuildConfig.TMDB_API_KEY
) : MovieRepository {
    // 1. Luồng đọc duy nhất: Luôn đọc trực tiếp từ Room DB
    override fun getTrendingMovies(): Flow<List<Movie>> {
        return movieCacheDao.getMoviesByCategory("trending").map {entities ->
            entities.map {it.toDomain()}
        }
    }
    // 2. Luồng Refresh: Gọi API -> Lưu dữ liệu mới vào Room DB
    override suspend fun refreshTrending() {
        val response = apiService.getTrending(apiKey)
        val movies = response.results.map {it.toMovie()}
        val entities = movies.map { it.toCachedEntity("trending") }
        // Xóa cache cũ của "trending" và chèn danh sách mới
        movieCacheDao.refreshCache("trending", entities)
    }

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

    override fun getFavorites(): Flow<List<Movie>> {
        return favoriteDao.getAll().map { entities ->
            entities.map {it.toDomain()}
        }
    }
    override fun isFavorite(movieId: Int): Flow<Boolean>{
        return favoriteDao.isFavorite(movieId)
    }
    override suspend fun toggleFavorite(movie: Movie) {
        val isFav = favoriteDao.isFavorite(movie.id).first()
        if (isFav){
            favoriteDao.delete(movie.id)
        }else{
            favoriteDao.insert(movie.toEntity())
        }
    }
}


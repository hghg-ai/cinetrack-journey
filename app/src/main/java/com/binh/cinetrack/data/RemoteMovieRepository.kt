package com.binh.cinetrack.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.binh.cinetrack.BuildConfig
import com.binh.cinetrack.data.local.FavoriteDao
import com.binh.cinetrack.data.local.MovieCacheDao
import com.binh.cinetrack.data.mapper.toCachedEntity
import com.binh.cinetrack.data.mapper.toDomain
import com.binh.cinetrack.data.mapper.toEntity
import com.binh.cinetrack.data.mapper.toMovie
import com.binh.cinetrack.data.remote.dto.TmdbApiService
import com.binh.cinetrack.domain.Genre
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteMovieRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val movieCacheDao: MovieCacheDao,
    private val apiService: TmdbApiService
) : MovieRepository {

    private val apiKey: String = BuildConfig.TMDB_API_KEY

    // Phân trang Popular Paging 3
    override fun getPopularPaged(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )
        ) {
            PopularMoviesPagingSource(apiService, apiKey)
        }.flow
    }

    // 1. Luồng đọc duy nhất (SSOT): Luôn đọc trực tiếp từ Room DB
    override fun getTrendingMovies(): Flow<List<Movie>> {
        return movieCacheDao.getMoviesByCategory("trending").map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // 2. Luồng Refresh: Gọi API -> Lưu dữ liệu mới vào Room DB
    override suspend fun refreshTrending() {
        val response = apiService.getTrending(apiKey)
        val movies = response.results.map { it.toMovie() }
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
            entities.map { it.toDomain() }
        }
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return favoriteDao.isFavorite(movieId)
    }

    override suspend fun toggleFavorite(movie: Movie) {
        val isFav = favoriteDao.isFavorite(movie.id).first()
        if (isFav) {
            favoriteDao.delete(movie.id)
        } else {
            favoriteDao.insert(movie.toEntity())
        }
    }
}

package com.binh.cinetrack.data

import androidx.paging.PagingData
import com.binh.cinetrack.domain.Genre
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import com.binh.cinetrack.domain.sampleMovies
import com.binh.cinetrack.domain.search
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.IOException
import kotlin.random.Random

class FakeMovieRepository : MovieRepository {

    private suspend fun simulateNetwork() {
        delay(1000)
        if (Random.nextFloat() < 0.3f) {
            throw IOException("Mạng lởm rồi!")
        }
    }
    override fun getPopularPaged(): Flow<PagingData<Movie>> {
        return flowOf(PagingData.from(sampleMovies))
    }

    override fun getTrendingMovies(): Flow<List<Movie>> {
        return flowOf(sampleMovies)
    }

    override suspend fun refreshTrending() {
        simulateNetwork()
    }

    override suspend fun getMovies(): List<Movie> {
        simulateNetwork()
        return sampleMovies
    }

    override suspend fun getPopular(page: Int): List<Movie> {
        simulateNetwork()
        return sampleMovies
    }// bị thừa ra ở hiện tại

    override suspend fun getMovieDetail(id: Int): Movie? {
        simulateNetwork()
        return sampleMovies.find { it.id == id }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        simulateNetwork()
        if (query.isBlank()) return emptyList()
        return sampleMovies.search(query)
    }

    override suspend fun getGenres(): List<Genre> {
        return emptyList()
    }

    override fun getFavorites(): Flow<List<Movie>> {
        return flowOf(emptyList())
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return flowOf(false)
    }

    override suspend fun toggleFavorite(movie: Movie) {}
}

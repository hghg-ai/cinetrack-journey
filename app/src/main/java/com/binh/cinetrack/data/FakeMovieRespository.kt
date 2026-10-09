package com.binh.cinetrack.data

import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.sampleMovies
import com.binh.cinetrack.domain.search
import kotlinx.coroutines.delay
import java.io.IOException
import kotlin.random.Random

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovieById(id: Int): Movie?
    suspend fun searchMovies(query: String): List<Movie>
}

class FakeMovieRepository : MovieRepository {

    // Giả lập mạng chậm 1 giây và 30% xác suất ném lỗi IOException
    private suspend fun simulateNetwork() {
        delay(1000)
        if (Random.nextFloat() < 0.3f) {
            throw IOException("Mạng lởm rồi!")
        }
    }

    override suspend fun getMovies(): List<Movie> {
        simulateNetwork()
        return sampleMovies
    }

    override suspend fun getMovieById(id: Int): Movie? {
        simulateNetwork()
        return sampleMovies.find { it.id == id }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        simulateNetwork()
        if (query.isBlank()) return emptyList()
        return sampleMovies.search(query)
    }
}
package com.binh.cinetrack.fakes

import androidx.paging.PagingData
import com.binh.cinetrack.domain.Genre
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.io.IOException

class FakeMovieRepository : MovieRepository {
    // Cho phép cài trước danh sách phim trả về
    var moviesToReturn = listOf(
        Movie(id = 1, title = "Fake Movie 1", overview = "", posterPath = "",
         backdropPath = "", rating = 8.0, releaseDate = "2024", genreIds = emptyList(),
            popularity = 10.0)
    )
    // Cờ ra lệnh "lần tới ném lỗi"
    var shouldThrowError = false

    //Lưu lại lịch sử từ khóa tìm kếm để kiểm tra Debounce
    val searchCalls = mutableListOf<String>()

    private val favoriteIds = mutableListOf<Int>()

    override fun getTrendingMovies(): Flow<List<Movie>> = flow {
        if (shouldThrowError) throw IOException("Mang lởm rồi!")
        emit(moviesToReturn)
    }

    override suspend fun refreshTrending() {
        if (shouldThrowError) throw IOException("Mạng lởm rồi!")
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        searchCalls.add(query)// Lược sử gọi tìm kiếm
        if (shouldThrowError) throw IOException("Lỗi tìm kiếm")
        return moviesToReturn.filter { it.title.contains(query, ignoreCase = true) }
    }

    override fun getFavorites(): Flow<List<Movie>> {
        return flowOf(moviesToReturn.filter { favoriteIds.contains(it.id) })
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return flowOf(favoriteIds.contains(movieId))
    }

    override suspend fun toggleFavorite(movie: Movie) {
        if (favoriteIds.contains(movie.id)) favoriteIds.remove(movie.id)
        else favoriteIds.add(movie.id)
    }

    override fun getPopularPaged(): Flow<PagingData<Movie>> = flowOf(PagingData.from(moviesToReturn))
    override suspend fun getMovies(): List<Movie> = moviesToReturn
    override suspend fun getPopular(page: Int): List<Movie> = moviesToReturn
    override suspend fun getMovieDetail(id: Int): Movie? = moviesToReturn.find { it.id == id }
    override suspend fun getGenres(): List<Genre> = emptyList()
}

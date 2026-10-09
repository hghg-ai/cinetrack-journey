import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.io.File

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getPopular(page: Int = 1): List<Movie>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieDetail(id: Int):Movie?
    suspend fun getGenres(): List<Genre>
}
class FakeMovieRepository : MovieRepository {
    private val jsonParser = Json{ignoreUnknownKeys = true}
    private fun loadMoviesFromJson(): List<Movie> {
        val file = File("src/main/resources/movies.json")
        if (!file.exists())
        return emptyList()
        return try {
            val jsonString = file.readText()
            val response = jsonParser.decodeFromString<MovieResponseDto>(jsonString)
            response.results.map { it.toMovie() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    override suspend fun getTrending(): List<Movie> {
        delay(1000L) // Giả lập độ trễ mạng 1 giây
        return loadMoviesFromJson().sortedByDescending{ it.popularity }
    }

    override suspend fun search(query: String): List<Movie> {
        delay(1000L) // Giả lập độ trễ mạng 1 giây
        return loadMoviesFromJson().filter {
            it.title.contains(query, ignoreCase = true)
        }
    }
}




import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.io.File

interface MovieRepository {
    suspend fun getTrending(): List<Movie>
    suspend fun search(query: String): List<Movie>
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




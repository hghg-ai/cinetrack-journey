import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class QueryEngineTest {

    private lateinit var sampleMovies: List<Movie>

    @BeforeEach
    fun setUp() {
        sampleMovies = listOf(
            Movie(1, "Pacific Rim", "Trận chiến Kaiju...",
                6.9, "2013-07-19", listOf(1, 2), 779.0),
            Movie(2, "Pirates of the Caribbean", "Jack Sparrow...",
                8.1, "2003-07-09", listOf(5, 2), 887.0),
            Movie(3, "Scary Movie 1", "Parody horror...",
                6.3, "2000-07-07", listOf(3, 4), 898.0),
            Movie(4, "Scary Movie 2", "Haunted mansion...",
                5.4, "2001-07-04", listOf(3, 4), 676.8),
            Movie(5, "White Chicks", "FBI agents...",
                6.0, "2004-06-23", listOf(3), 786.8)
        )
    }

    @Test
    @DisplayName("Search trả về đúng danh sách khi tìm thấy từ khóa")
    fun testSearchSuccess() {
        val results = sampleMovies.search("Scary")
        assertEquals(2, results.size)
        assertEquals("Scary Movie 1", results[0].title)
    }

    @Test
    @DisplayName("Search không phân biệt chữ hoa chữ thường")
    fun testSearchCaseInsensitive() {
        val resultsLower = sampleMovies.search("pacific")
        val resultsUpper = sampleMovies.search("PACIFIC")

        assertEquals(1, resultsLower.size)
        assertEquals(resultsLower, resultsUpper)
    }

    @Test
    @DisplayName("Top 5 theo Rating trả danh sách đúng thứ tự giảm dần")
    fun testTop5Rating() {
        val top5 = sampleMovies.top5ByRating()

        assertEquals(5, top5.size)
        assertEquals("Pirates of the Caribbean", top5[0].title) // Highest rating: 8.1
        assertEquals("Scary Movie 2", top5[4].title)            // Lowest in top 5: 5.4
    }

    @Test
    @DisplayName("Filter theo Genre không tồn tại trả về danh sách rỗng")
    fun testFilterGenreNonExistent() {
        val results = sampleMovies.filterByGenre(999)
        assertTrue(results.isEmpty())
    }
}
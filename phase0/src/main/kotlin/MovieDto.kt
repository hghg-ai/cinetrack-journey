import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieResponseDto(
    val results: List<MovieDto> = emptyList()
)
@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String? = "",
    val popularity: Double = 0.0,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = "",
    @SerialName("vote_average") val voteAverage: Double? = 0.0,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
)
fun MovieDto.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview ?:"",
        rating = this.voteAverage ?: 0.0,
        popularity = this.popularity,
        releaseDate = this.releaseDate ?: "",
        genreIds = this.genreIds
    )
}
// 4.json parser/mapper
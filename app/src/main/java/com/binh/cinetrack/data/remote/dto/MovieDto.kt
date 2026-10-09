package com.binh.cinetrack.data.remote.dto

import com.binh.cinetrack.domain.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseDto(
    val results: List<MovieDto> = emptyList()
)
@Serializable
data class MovieDetailDto(
    val id: Int,
    val title: String,
    val overview: String = "",
    val popularity: Double = 0.0,
    @SerialName("poter_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
   val genres: List<GenreDto> = emptyList()
)
@Serializable
data class GenreDto(val id: Int,
    val name: String)
@Serializable
data class GenreListResponseDto(
    val genres: List<GenreDto> = emptyList()
)
@Serializable
data class MovieDto(
    val id:Int,
    val title: String,
    val overview: String? = "",
    val popularity: Double = 0.0,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = "",
    @SerialName("vote_average") val voteAverage: Double? = 0.0,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList()
)

package com.binh.cinetrack.data.mapper

import com.binh.cinetrack.data.remote.dto.MovieDetailDto
import com.binh.cinetrack.data.remote.dto.MovieDto
import com.binh.cinetrack.domain.Movie

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"

fun MovieDto.toMovie() : Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview ?: "",
        rating = this.voteAverage ?: 0.0,
        popularity = this.popularity,
        releaseDate = this.releaseDate ?: "",
        genreIds = this.genreIds,
        posterPath = this.posterPath?.let { "$POSTER_BASE_URL$it" } ?: "",
        backdropPath = this.backdropPath?.let { "$BACKDROP_BASE_URL$it" } ?: ""
    )
}
fun MovieDetailDto.toMovie(): Movie{
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview ?:"",
        rating = this.voteAverage ?: 0.0,
        popularity = this.popularity,
        releaseDate = this.releaseDate ?: "",
        genreIds = this.genres.map {it.id},
        posterPath = this.posterPath?.let { "$POSTER_BASE_URL$it" } ?: "",
        backdropPath = this.backdropPath?.let { "$BACKDROP_BASE_URL$it" } ?: ""
    )
}
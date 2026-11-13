package com.binh.cinetrack.data.mapper

import com.binh.cinetrack.data.local.CachedMovieEntity
import com.binh.cinetrack.data.remote.dto.CastDto
import com.binh.cinetrack.data.remote.dto.MovieDetailDto
import com.binh.cinetrack.data.remote.dto.MovieDto
import com.binh.cinetrack.domain.Cast
import com.binh.cinetrack.domain.Movie

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"

private const val PROFILE_BASE_URL = "https://image.tmdb.org/t/p/w185"

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
fun CachedMovieEntity.toDomain(): Movie{
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        rating = this.rating,
        popularity = 0.0,
        releaseDate = this.releaseDate,
        genreIds = emptyList(),
        posterPath = this.posterPath,
        backdropPath = this.backdropPath
    )
}

fun Movie.toCachedEntity(category: String) : CachedMovieEntity{
    return CachedMovieEntity(
        id = this.id,
        category = category,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        rating = this.rating,
        releaseDate = this.releaseDate
    )
}
fun CastDto.toCast(): Cast{
    return Cast(
        id = this.id,
        name = this.name,
        character = this.character,
        profilePath = this.profilePath?.let {"$PROFILE_BASE_URL$it"} ?: ""
    )
}
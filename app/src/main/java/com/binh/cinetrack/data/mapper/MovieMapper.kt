package com.binh.cinetrack.data.mapper

import com.binh.cinetrack.data.local.CachedMovieEntity
import com.binh.cinetrack.data.remote.dto.CastDto
import com.binh.cinetrack.data.remote.dto.MovieDetailDto
import com.binh.cinetrack.data.remote.dto.MovieDto
import com.binh.cinetrack.domain.Cast
import com.binh.cinetrack.domain.Movie

/**
 * Các hàm Mappers chuyển đổi qua lại giữa DTO (Server), Entity (Room DB) và Domain Model (Sử dụng trên UI).
 */

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"
private const val PROFILE_BASE_URL = "https://image.tmdb.org/t/p/w185"

// Chuyển MovieDto (Server) -> Movie (Domain)
fun MovieDto.toMovie(): Movie {
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

// Chuyển MovieDetailDto (Server) -> Movie (Domain)
fun MovieDetailDto.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview ?: "",
        rating = this.voteAverage ?: 0.0,
        popularity = this.popularity,
        releaseDate = this.releaseDate ?: "",
        genreIds = this.genres.map { it.id },
        posterPath = this.posterPath?.let { "$POSTER_BASE_URL$it" } ?: "",
        backdropPath = this.backdropPath?.let { "$BACKDROP_BASE_URL$it" } ?: ""
    )
}

// Chuyển CachedMovieEntity (Room DB Cache) -> Movie (Domain)
fun CachedMovieEntity.toDomain(): Movie {
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

// Chuyển Movie (Domain) -> CachedMovieEntity (Room DB Cache)
fun Movie.toCachedEntity(category: String): CachedMovieEntity {
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

// Chuyển CastDto (Server) -> Cast (Domain)
fun CastDto.toCast(): Cast {
    return Cast(
        id = this.id,
        name = this.name,
        character = this.character,
        profilePath = this.profilePath?.let { "$PROFILE_BASE_URL$it" } ?: ""
    )
}

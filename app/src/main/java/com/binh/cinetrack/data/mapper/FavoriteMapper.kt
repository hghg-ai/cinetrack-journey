package com.binh.cinetrack.data.mapper

import com.binh.cinetrack.data.local.FavoriteMovieEntity
import com.binh.cinetrack.domain.Movie

/**
 * Mapper chuyển đổi qua lại giữa FavoriteMovieEntity (Room DB Yêu thích) và Movie (Domain Model).
 */

// Chuyển FavoriteMovieEntity (Room DB) -> Movie (Domain)
fun FavoriteMovieEntity.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        posterPath = this.posterUrl ?: "",
        rating = this.rating,
        releaseDate = this.releaseDate,
        overview = "",
        backdropPath = "",
        genreIds = emptyList(),
        popularity = 0.0
    )
}

// Chuyển Movie (Domain) -> FavoriteMovieEntity (Room DB)
fun Movie.toEntity(): FavoriteMovieEntity {
    return FavoriteMovieEntity(
        id = this.id,
        title = this.title,
        posterUrl = this.posterPath,
        rating = this.rating,
        releaseDate = this.releaseDate
    )
}

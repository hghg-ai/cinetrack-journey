package com.binh.cinetrack.data.mapper

import com.binh.cinetrack.data.local.FavoriteMovieEntity
import com.binh.cinetrack.domain.Movie

fun FavoriteMovieEntity.toDomain() : Movie {
    return Movie(
        id = this.id,
        title = this.title,
        posterPath = this.posterUrl ?:"",
        rating = this.rating,
        releaseDate = this.releaseDate,
        overview = "",
        backdropPath = "",
        genreIds = emptyList(),
        popularity = 0.0
    )
}
fun Movie.toEntity(): FavoriteMovieEntity{
    return FavoriteMovieEntity(
        id = this.id,
        title = this.title,
        posterUrl = this.posterPath,
        rating = this.rating,
        releaseDate = this.releaseDate
    )
}
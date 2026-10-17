package com.binh.cinetrack.data.local

import android.icu.text.CaseMap
import androidx.room.Entity

@Entity(
    tableName = "cached_movies",
    primaryKeys = ["id","category"]
)
data class CachedMovieEntity(
    val id: Int,
    val category: String,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val rating: Double,
    val releaseDate: String,
    val cachedAt: Long = System.currentTimeMillis()
)

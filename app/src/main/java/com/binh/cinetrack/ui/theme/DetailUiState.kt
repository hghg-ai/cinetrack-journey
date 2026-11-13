package com.binh.cinetrack.ui.theme

import androidx.compose.ui.graphics.Canvas
import com.binh.cinetrack.domain.Cast
import com.binh.cinetrack.domain.Genre
import com.binh.cinetrack.domain.Movie

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val movie: Movie,
                       val genres: List<Genre> ,
                       val cast: List<Cast>,
                       val similarMovies: List<Movie>) : DetailUiState
    data class Error(val message: String) : DetailUiState

}
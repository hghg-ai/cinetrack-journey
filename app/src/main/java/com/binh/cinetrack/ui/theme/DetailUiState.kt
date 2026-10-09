package com.binh.cinetrack.ui.theme

import android.os.Message
import com.binh.cinetrack.domain.Genre
import com.binh.cinetrack.domain.Movie

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val movie: Movie,val genres: List<Genre> = emptyList()) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
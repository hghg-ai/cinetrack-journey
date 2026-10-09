package com.binh.cinetrack.ui.theme

import android.os.Message
import com.binh.cinetrack.domain.Movie

sealed interface HomeUiState {
    object Loading: HomeUiState
    data class Success(val movies: List<Movie>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
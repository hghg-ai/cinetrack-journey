package com.binh.cinetrack.ui.theme

import android.os.Message
import com.binh.cinetrack.domain.Movie

sealed interface HomeUiState {
    object Loading: HomeUiState
    data class Success(val movies: List<Movie>,
                       val isOffline: Boolean = false,
        val showOnlineBanner: Boolean = false) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
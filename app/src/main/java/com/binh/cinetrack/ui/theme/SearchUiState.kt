package com.binh.cinetrack.ui.theme

import android.os.Message
import com.binh.cinetrack.domain.Movie

sealed interface SearchUiState{
    object Idle : SearchUiState
    object Loading : SearchUiState
    data class Success(val movies : List<Movie>): SearchUiState
    object Empty : SearchUiState
    data class  Error(val message : String): SearchUiState
}
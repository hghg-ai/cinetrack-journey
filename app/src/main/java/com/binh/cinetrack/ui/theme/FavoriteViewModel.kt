package com.binh.cinetrack.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel quản lý màn hình Phim Yêu Thích (Favorites Screen).
 * Lắng nghe trực tiếp luồng danh sách phim yêu thích từ Room Database qua Flow.
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    // Danh sách các phim yêu thích phát dạng StateFlow thời gian thực
    val favorite: StateFlow<List<Movie>> = repository.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

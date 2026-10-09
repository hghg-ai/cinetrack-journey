package com.binh.cinetrack.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.data.RemoteMovieRepository
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val respository: MovieRepository = RemoteMovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val movies = respository.getMovies()
                _uiState.value = HomeUiState.Success(movies)
            } catch (e: IOException) {
                _uiState.value = HomeUiState.Error(e.message ?: "Đã có lỗi xảy ra")
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Lỗi ko xác định: ${e.message}")
            }
        }
    }
}

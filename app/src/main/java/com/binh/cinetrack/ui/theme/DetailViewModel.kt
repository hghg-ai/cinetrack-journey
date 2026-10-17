package com.binh.cinetrack.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.data.RemoteMovieRepository
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val _movieId = MutableStateFlow<Int?>(null)

    val isFavorite: StateFlow<Boolean> = _movieId.flatMapLatest{
        id -> if (id != null) repository.isFavorite(id) else
            kotlinx.coroutines.flow.flowOf(false)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    fun toggleFavorite(movie: Movie){
        viewModelScope.launch {
            repository.toggleFavorite(movie)
        }
    }

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadMovieDetail(movieId: Int) {
        Log.d("DetailViewModel", "loadMovieDetail() được gọi với movieId = $movieId")
        _movieId.value = movieId
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val movieDeferred = async { repository.getMovieDetail(movieId) }
                val genresDeferred = async { repository.getGenres() }

                val movie = movieDeferred.await()
                val genres = genresDeferred.await()

                if (movie != null) {
                    _uiState.value = DetailUiState.Success(movie, genres)
                } else {
                    _uiState.value = DetailUiState.Error("Không có thông tin phim này")
                }
            } catch (e: IOException) {
                _uiState.value = DetailUiState.Error(e.message ?: "Lỗi kết nối mạng")
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error("Lỗi không xác định: ${e.message}")
            }
        }
    }
}

package com.binh.cinetrack.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.data.RemoteMovieRepository
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(
    private val repository: MovieRepository = RemoteMovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadMovieDetail(movieId: Int) {
        Log.d("DetailViewModel", "loadMovieDetail() được gọi với movieId = $movieId")
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

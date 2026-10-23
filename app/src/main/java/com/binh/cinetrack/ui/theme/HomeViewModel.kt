package com.binh.cinetrack.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val respository: MovieRepository
) : ViewModel() {

    val popularMovies: Flow<PagingData<Movie>> = respository
        .getPopularPaged()
        .cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Cờ trạng thái lỗi mạng
    private val isOfflineState = MutableStateFlow(false)
    private var wasOffline = false

    init {
        loadAndRefresh()
    }

    fun loadAndRefresh() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            // 1. Luồng A: Độc lập lắng nghe dữ liệu từ Room Database
            launch {
                respository.getTrendingMovies().collect { cachedMovies ->
                    if (cachedMovies.isNotEmpty()) {
                      val currentStage = _uiState.value
                        val showBanner = if (currentStage is HomeUiState.Success) currentStage
                            .showOnlineBanner else false
                        _uiState.value = HomeUiState.Success(
                            movies = cachedMovies,
                            isOffline = isOfflineState.value,
                            showOnlineBanner = showBanner
                        )
                    } else if (isOfflineState.value) {
                        // Nếu DB rỗng VÀ mạng bị lỗi -> Hiện màn hình Error
                        _uiState.value = HomeUiState.Error(
                            "Không có kết nối mạng và chưa có dữ liệu cache")
                    }
                }
            }

            // 2. Luồng B: Độc lập gọi API để làm mới cache vào Room DB
            launch {
                try {
                    respository.refreshTrending()
                    if (isOfflineState.value || wasOffline) {
                        wasOffline = false
                        isOfflineState.value = false
                        // Khi refresh thành công, nếu đang ở Success thì cập nhật isOffline = false
                        val currentStage = _uiState.value
                        if (currentStage is HomeUiState.Success) {
                            _uiState.value =
                                currentStage.copy(isOffline = false, showOnlineBanner = true)
                        }
                        launch {
                            delay(3000L)
                            val stateAfterDelay = _uiState.value
                            if (stateAfterDelay is HomeUiState.Success) {
                                _uiState.value = stateAfterDelay.copy(showOnlineBanner = false)
                            }
                        }
                    }else {
                        isOfflineState.value = false
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Lỗi nạp mạng: ${e.message}", e)
                    isOfflineState.value = true
                    // Khi gặp lỗi mạng, nếu đang có cache
                    // + thì giữ nguyên danh sách và bật cờ isOffline = true
                    wasOffline = true// Ghi nhớ từng bị lỗi mạng
                    val currentStage = _uiState.value
                    if (currentStage is HomeUiState.Success) {
                        _uiState.value = currentStage.copy(isOffline = true, showOnlineBanner = false)
                    }
                }
            }
        }
    }
}

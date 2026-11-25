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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel quản lý logic dữ liệu màn hình Trang chủ (HomeScreen).
 * Xử lý chế độ xem Offline SSOT, trạng thái nạp mạng và giữ nguyên vị trí cuộn khi Refresh.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val respository: MovieRepository
) : ViewModel() {

    // Luồng dữ liệu danh sách phim Popular phân trang bằng Paging 3
    val popularMovies: Flow<PagingData<Movie>> = respository
        .getPopularPaged()
        .cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Cờ báo trạng thái đang nạp lại mạng (dùng xoay Icon Refresh 🔄 trên BottomBar)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // Cờ trạng thái lỗi mạng
    private val isOfflineState = MutableStateFlow(false)
    private var wasOffline = false

    init {
        loadAndRefresh()
    }

    fun loadAndRefresh() {
        viewModelScope.launch {
            
            // 🟢 KỸ THUẬT GIỮ NGUYÊN VỊ TRÍ CUỘN KHI REFRESH:
            // Chỉ chuyển trạng thái màn hình về Loading nếu chưa có dữ liệu (khi mở app lần đầu).
            // Khi ứng dụng đã có dữ liệu (HomeUiState.Success), KHÔNG chuyển về Loading nữa
            // để tránh hủy bỏ LazyVerticalGrid khỏi cây giao diện, nhờ đó vị trí cuộn của người dùng được giữ nguyên 100%!
            if (_uiState.value !is HomeUiState.Success) {
                _uiState.value = HomeUiState.Loading
            }

            // 1. Luồng A: Độc lập lắng nghe dữ liệu từ Room Database Cache (Offline SSOT)
            launch {
                respository.getTrendingMovies()
                    .catch { e ->
                        _uiState.value = HomeUiState.Error(e.message ?: "Đã có lỗi xảy ra")
                    }
                    .collect { cachedMovies ->
                        if (cachedMovies.isNotEmpty()) {
                            val currentStage = _uiState.value
                            val showBanner = if (currentStage is HomeUiState.Success) currentStage.showOnlineBanner else false
                            _uiState.value = HomeUiState.Success(
                                movies = cachedMovies,
                                isOffline = isOfflineState.value,
                                showOnlineBanner = showBanner
                            )
                        } else if (isOfflineState.value) {
                            _uiState.value = HomeUiState.Error("Không có kết nối mạng và chưa có dữ liệu cache")
                        }
                    }
            }

            // 2. Luồng B: Độc lập gọi API để làm mới cache vào Room DB
            launch {
                _isRefreshing.value = true
                try {
                    respository.refreshTrending()
                    if (isOfflineState.value || wasOffline) {
                        wasOffline = false
                        isOfflineState.value = false
                        val currentStage = _uiState.value
                        if (currentStage is HomeUiState.Success) {
                            _uiState.value = currentStage.copy(isOffline = false, showOnlineBanner = true)
                        }
                        launch {
                            delay(3000L)
                            val stateAfterDelay = _uiState.value
                            if (stateAfterDelay is HomeUiState.Success) {
                                _uiState.value = stateAfterDelay.copy(showOnlineBanner = false)
                            }
                        }
                    } else {
                        isOfflineState.value = false
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Lỗi nạp mạng: ${e.message}", e)
                    isOfflineState.value = true
                    wasOffline = true
                    val currentStage = _uiState.value
                    if (currentStage is HomeUiState.Success) {
                        _uiState.value = currentStage.copy(isOffline = true, showOnlineBanner = false)
                    } else {
                        _uiState.value = HomeUiState.Error(e.message ?: "Không có kết nối mạng")
                    }
                } finally {
                    _isRefreshing.value = false
                }
            }
        }
    }
}

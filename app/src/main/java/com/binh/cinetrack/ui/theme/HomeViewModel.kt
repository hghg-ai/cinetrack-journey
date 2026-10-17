package com.binh.cinetrack.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val respository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadAndRefresh()
    }

    fun loadAndRefresh() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            // BƯỚC A: Lắng nghe liên tục dữ liệu phát ra từ Room DB
            respository.getTrendingMovies().collect { cachedMovies ->
                // BƯỚC B: Thử gọi API để làm mới dữ liệu
                try {
                    respository.refreshTrending()
                    // Nếu refresh thành công: Room DB tự động được cập nhật
                    // -> Flow phát ra danh sách mới -> Hiện UI Success (isOffline = false)
                    _uiState.value = HomeUiState.Success(movies = cachedMovies, isOffline = false)
                } catch (e : Exception){
                    // BƯỚC C: Khi gặp lỗi mạng (IOException / ném ngoại lệ)
                    if (cachedMovies.isNotEmpty()){
                        // Nếu trong Cache ĐÃ CÓ dữ liệu -> Hiện phim kèm cờ isOffline = true
                        _uiState.value = HomeUiState.Success(movies = cachedMovies, isOffline = true)
                    } else {
                        // Nếu Cache RỖNG -> Hiện màn hình Error
                        _uiState.value = HomeUiState.Error(
                            e.message ?: "Không có kết nối mạng và chưa có dữ liệu cache")
                    }
                }
            }
        }
    }
}

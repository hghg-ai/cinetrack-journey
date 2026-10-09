package com.binh.cinetrack.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.data.RemoteMovieRepository
import com.binh.cinetrack.domain.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val repository: MovieRepository = RemoteMovieRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    flow {
                        if (query.isBlank()) {
                            emit(SearchUiState.Idle)
                        } else {
                            Log.d("SearchViewModel", "Thực hiện API tìm kiếm với từ khóa: '$query'")
                            emit(SearchUiState.Loading)
                            try {
                                val results = repository.searchMovies(query)
                                if (results.isEmpty()) {
                                    emit(SearchUiState.Empty)
                                } else {
                                    emit(SearchUiState.Success(results))
                                }
                            } catch (e: IOException) {
                                emit(SearchUiState.Error(e.message ?: "Lỗi kết nối mạng"))
                            } catch (e: Exception) {
                                emit(SearchUiState.Error("Đã có lỗi xảy ra"))
                            }
                        }
                    }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun retrySearch() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isNotBlank()) {
            viewModelScope.launch {
                _uiState.value = SearchUiState.Loading
                try {
                    val results = repository.searchMovies(currentQuery)
                    if (results.isEmpty()) {
                        _uiState.value = SearchUiState.Empty
                    } else {
                        _uiState.value = SearchUiState.Success(results)
                    }
                } catch (e: IOException) {
                    _uiState.value = SearchUiState.Error(e.message ?: "Lỗi kết nối mạng")
                } catch (e: Exception) {
                    _uiState.value = SearchUiState.Error("Đã có lỗi xảy ra")
                }
            }
        }
    }
}

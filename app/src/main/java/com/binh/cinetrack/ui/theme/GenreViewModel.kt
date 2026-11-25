package com.binh.cinetrack.ui.theme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel quản lý logic trang Duyệt Phim Theo Thể Loại (Genre Screen).
 * Lấy genreId và genreName truyền từ Navigation SavedStateHandle và cung cấp luồng phân trang Paging 3.
 */
@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    // Lấy genreId và genreName truyền từ Navigation Route
    val genreId: Int = checkNotNull(savedStateHandle["genreId"])
    val genreName: String = savedStateHandle["genreName"] ?: "Thể loại"

    // Luồng phân trang Paging 3 lấy danh sách phim thuộc thể loại
    val movies: Flow<PagingData<Movie>> = repository.getMoviesByGenrePaged(genreId)
        .cachedIn(viewModelScope)
}

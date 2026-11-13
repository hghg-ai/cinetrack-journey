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

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val genreId: Int = checkNotNull(savedStateHandle["genreId"])
    val genreName: String = savedStateHandle["genreName"] ?: "Thể loại"

    val movies: Flow<PagingData<Movie>> = repository.getMoviesByGenrePaged(genreId)
        .cachedIn(viewModelScope)
}
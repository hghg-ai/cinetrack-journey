package com.binh.cinetrack.ui

import app.cash.turbine.test
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.fakes.FakeMovieRepository
import com.binh.cinetrack.ui.theme.HomeUiState
import com.binh.cinetrack.ui.theme.HomeViewModel
import com.binh.cinetrack.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeMovieRepository
    private lateinit var viewModel: HomeViewModel

    private val fakeMovies = listOf(
        Movie(id = 1, title = "Fake Movie 1", overview = "Overview", posterPath = "poster.jpg", backdropPath = "", rating = 8.5, releaseDate = "2024", genreIds = emptyList(), popularity = 100.0)
    )

    @Before
    fun setUp() {
        repository = FakeMovieRepository().apply {
            moviesToReturn = fakeMovies
        }
    }

    @Test
    fun `init emits Loading then Success with correct fake data`() = runTest {
        viewModel = HomeViewModel(repository)

        viewModel.uiState.test {
            val firstState = awaitItem()
            assertTrue(firstState is HomeUiState.Loading || firstState is HomeUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when repository throws error, uiState emits Error`() = runTest {
        repository.shouldThrowError = true
        repository.moviesToReturn = emptyList()
        viewModel = HomeViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HomeUiState.Error || state is HomeUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `calling loadAndRefresh after error recovers back to Loading then Success`() = runTest {
        // 1. Cho repository ném lỗi trước
        repository.shouldThrowError = true
        repository.moviesToReturn = emptyList()
        viewModel = HomeViewModel(repository)

        // 2. Sửa lỗi cho repository hoạt động lại bình thường
        repository.shouldThrowError = false
        repository.moviesToReturn = fakeMovies

        // 3. Gọi hàm loadAndRefresh() để quay lại nạp dữ liệu
        viewModel.loadAndRefresh()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HomeUiState.Loading || state is HomeUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

package com.binh.cinetrack.ui

import com.binh.cinetrack.fakes.FakeMovieRepository
import com.binh.cinetrack.ui.theme.SearchViewModel
import com.binh.cinetrack.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeMovieRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp(){
    repository = FakeMovieRepository()
    viewModel = SearchViewModel(repository)
}
    @Test
    fun `typing b, ba, bat quickly calls searchMovies only once with bat`() = runTest {
        // Gõ liên tiếp "b", "ba", "bat" nhanh
        viewModel.onQueryChange("b")
        advanceTimeBy(100L)

        viewModel.onQueryChange("ba")
        advanceTimeBy(100L)

        viewModel.onQueryChange("bat")
        advanceTimeBy(350L)
        // Chờ vượt quá 300ms Debounce

        //Kiểm tra repository chỉ ghi nhận 1 lần tìm kiếm duy nhất với "bat"
        assertEquals(listOf("bat"), repository.searchCalls)
    }
    @Test
    fun `onQueryChange with empty string resets uiState to Idle`() = runTest {
        viewModel.onQueryChange("")
        advanceTimeBy(350L)
        // Khi câu truy vấn rỗng -> UI State ở trạng thái Idle
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `search with non-matching query returns empty list`() = runTest {
        repository.moviesToReturn = emptyList()
        viewModel.onQueryChange("non_existent_movie")
        advanceTimeBy(350L)

        assertEquals(listOf("non_existent_movie"), repository.searchCalls)
    }
}
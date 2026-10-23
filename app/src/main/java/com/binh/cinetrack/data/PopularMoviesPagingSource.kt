package com.binh.cinetrack.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.binh.cinetrack.data.mapper.toMovie
import com.binh.cinetrack.data.remote.dto.TmdbApiService
import com.binh.cinetrack.domain.Movie
import okhttp3.internal.dns.DnsMessage.Companion.response
import okio.IOException

class PopularMoviesPagingSource(
    private val apiService: TmdbApiService,
    private val apiKey : String
): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try{
            // 1. Gọi API lấy danh sách phim phổ biến theo trang
            val response = apiService.getPopular(apiKey, page)
            val movies = response.results.map {it.toMovie()}
            // 2. Trả về kết quả trang thành công
            LoadResult.Page(
                data = movies,
                prevKey = if(page == 1) null else page -1,
                nextKey = if (movies.isEmpty()) null else page +1
            )
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}
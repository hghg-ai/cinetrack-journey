package com.binh.cinetrack.data.remote.dto

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface Retrofit định nghĩa tất cả các REST API Endpoints gọi tới Server TMDB (The Movie Database).
 */
interface TmdbApiService {

    // 1. Lấy danh sách phim Trending trong ngày
    @GET("trending/movie/day")
    suspend fun getTrending(
        @Query("api_key") apiKey: String
    ): MovieResponseDto

    // 2. Lấy danh sách phim Phổ biến (Popular) có phân trang
    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponseDto

    // 3. Tìm kiếm phim theo từ khóa
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MovieResponseDto

    // 4. Lấy thông tin chi tiết của 1 bộ phim theo ID
    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailDto

    // 5. Lấy danh sách tất cả các Thể loại phim (Genres)
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): GenreListResponseDto

    // 6. Lấy danh sách Diễn viên (Cast / Credits) của một bộ phim
    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): CreditsResponseDto

    // 7. Lấy danh sách các Phim tương tự (Similar Movies)
    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieResponseDto

    // 8. Khám phá & Lọc danh sách phim theo ID Thể loại có phân trang
    @GET("discover/movie")
    suspend fun discoverMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreID: Int,
        @Query("page") page: Int = 1
    ): MovieResponseDto
}

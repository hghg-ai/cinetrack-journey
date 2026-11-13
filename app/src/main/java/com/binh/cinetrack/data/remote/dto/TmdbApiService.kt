package com.binh.cinetrack.data.remote.dto


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("trending/movie/day")
    suspend fun getTrending(
        @Query("api_key") apiKey: String
    ): MovieResponseDto

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MovieResponseDto

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailDto

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): GenreListResponseDto

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): CreditsResponseDto

    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieResponseDto
    @GET("discover/movie")
    suspend fun discoverMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreID: Int,
        @Query("page") page: Int = 1
    ): MovieResponseDto
}
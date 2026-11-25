package com.binh.cinetrack.data.local

import androidx.room.Entity

/**
 * Thực thể bảng `cached_movies` lưu trữ danh sách phim tạm thời vào Room Database.
 * Phục vụ cơ chế Single Source of Truth (SSOT) giúp ứng dụng xem phim Offline khi mất mạng.
 */
@Entity(
    tableName = "cached_movies",
    primaryKeys = ["id", "category"]
)
data class CachedMovieEntity(
    val id: Int,                     // ID phim từ TMDB Server
    val category: String,           // Danh mục phim (ví dụ: "trending", "popular")
    val title: String,              // Tên bộ phim
    val overview: String,           // Tóm tắt nội dung phim
    val posterPath: String,         // Đường dẫn ảnh Poster
    val backdropPath: String,       // Đường dẫn ảnh Banner nền
    val rating: Double,             // Điểm đánh giá
    val releaseDate: String,        // Ngày phát hành
    val cachedAt: Long = System.currentTimeMillis() // Thời điểm lưu cache
)

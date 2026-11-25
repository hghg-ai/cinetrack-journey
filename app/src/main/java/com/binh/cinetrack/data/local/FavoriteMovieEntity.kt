package com.binh.cinetrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Thực thể bảng `favorite_movies` trong Room Database.
 * Lưu trữ danh sách phim mà người dùng đã bấm thả tim Yêu Thích.
 */
@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,                         // ID phim duy nhất
    val title: String,                               // Tên phim
    val posterUrl: String?,                          // Đường dẫn ảnh Poster
    val rating: Double,                              // Điểm đánh giá
    val releaseDate: String,                         // Ngày phát hành
    val addedAt: Long = System.currentTimeMillis()   // Thời điểm bấm lưu yêu thích
)

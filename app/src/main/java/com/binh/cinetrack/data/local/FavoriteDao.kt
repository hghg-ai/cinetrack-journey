package com.binh.cinetrack.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) quản lý các thao tác CRUD với bảng phim yêu thích (`favorite_movies`).
 */
@Dao
interface FavoriteDao {
    
    // 1. Lấy toàn bộ danh sách phim yêu thích theo thời gian mới nhất (Flow tự động cập nhật UI)
    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    fun getAll(): Flow<List<FavoriteMovieEntity>>

    // 2. Kiểm tra bộ phim cụ thể đã được lưu yêu thích chưa (Trả về Flow<Boolean>)
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun isFavorite(movieId: Int): Flow<Boolean>

    // 3. Thêm bộ phim mới vào danh sách yêu thích
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieEntity: FavoriteMovieEntity)

    // 4. Xóa bộ phim khỏi danh sách yêu thích theo ID
    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun delete(movieId: Int)
}

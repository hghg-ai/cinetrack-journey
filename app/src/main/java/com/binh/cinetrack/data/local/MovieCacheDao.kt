package com.binh.cinetrack.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieCacheDao {
    // 1. Luồng SSOT: Đọc dữ liệu từ DB ra theo dạng Flow (tự động phát dữ liệu khi DB có thay đổi)
    @Query("SELECT * FROM cached_movies WHERE category = :category ORDER BY cachedAt DESC")
    fun getMoviesByCategory(category: String): Flow<List<CachedMovieEntity>>
    // 2. Xóa dữ liệu cache cũ theo danh mục
    @Query("DELETE FROM cached_movies WHERE category = :category")
    suspend fun deleteByCategory(category: String)
    // 3. Chèn các danh sách entity mới vào Room DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<CachedMovieEntity>)
    // 4. Giao dịch nguyên tử (Transaction): Xóa danh mục cũ rồi chèn dữ liệu mới cùng lúc
    @Transaction
    suspend fun refreshCache(category: String, movies: List<CachedMovieEntity>) {
        deleteByCategory(category)
        insertAll(movies)
    }
}
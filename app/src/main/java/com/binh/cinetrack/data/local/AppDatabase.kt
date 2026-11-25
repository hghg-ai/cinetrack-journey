package com.binh.cinetrack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Cơ sở dữ liệu SQLite chính của ứng dụng CineTrack được quản lý bởi Room Database.
 * Lưu trữ 2 bảng:
 * 1. favorite_movies: Danh sách các phim yêu thích của người dùng.
 * 2. cached_movies: Bộ nhớ đệm (Cache) danh sách phim Trending để xem Offline (SSOT).
 */
@Database(
    entities = [FavoriteMovieEntity::class, CachedMovieEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    
    // Cung cấp các đối tượng DAO tương tác với cơ sở dữ liệu
    abstract fun favoriteDao(): FavoriteDao
    abstract fun movieCacheDao(): MovieCacheDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Hàm khởi tạo Singleton đảm bảo chỉ tạo duy nhất 1 thể hiện (Instance)
         * của AppDatabase trong suốt chu trình sống của ứng dụng.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cinetrack_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

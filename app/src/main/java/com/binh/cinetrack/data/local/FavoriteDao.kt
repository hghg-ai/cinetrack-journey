package com.binh.cinetrack.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

@Dao
interface FavoriteDao{
    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    fun getAll(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun isFavorite(movieId: Int): Flow<Boolean>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insert(movieEntity: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun delete(movieId: Int)
}
package com.binh.cinetrack.di

import android.content.Context
import com.binh.cinetrack.data.local.AppDatabase
import com.binh.cinetrack.data.local.FavoriteDao
import com.binh.cinetrack.data.local.MovieCacheDao
import com.binh.cinetrack.data.preferences.ThemePreferences
import com.binh.cinetrack.data.remote.dto.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Hilt Dependency Injection Module cấp SingletonComponent.
 * Cung cấp các thể hiện dùng chung toàn app như Retrofit, OkHttpClient, Room Database, DAOs và DataStore.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Cung cấp TmdbApiService (Retrofit REST Client)
    @Provides
    @Singleton
    fun provideTmdbApi(): TmdbApiService {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TmdbApiService::class.java)
    }

    // Cung cấp Room AppDatabase
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    // Cung cấp FavoriteDao
    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    // Cung cấp MovieCacheDao
    @Provides
    fun provideMovieCacheDao(db: AppDatabase): MovieCacheDao = db.movieCacheDao()

    // Cung cấp ThemePreferences (DataStore)
    @Provides
    @Singleton
    fun provideThemePreferences(@ApplicationContext context: Context): ThemePreferences {
        return ThemePreferences(context)
    }
}

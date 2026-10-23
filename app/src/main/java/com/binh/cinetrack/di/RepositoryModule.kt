package com.binh.cinetrack.di

import com.binh.cinetrack.data.RemoteMovieRepository
import com.binh.cinetrack.domain.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: RemoteMovieRepository) : MovieRepository
}
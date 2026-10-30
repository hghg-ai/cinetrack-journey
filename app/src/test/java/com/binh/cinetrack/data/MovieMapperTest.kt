package com.binh.cinetrack.data

import com.binh.cinetrack.data.mapper.toMovie
import com.binh.cinetrack.data.remote.dto.MovieDto
import org.junit.Assert.assertEquals
import org.junit.Test


class MovieMapperTest {

    @Test
    fun `toMovie correctly append poster URL prefix`(){
        val dto = MovieDto(id = 1, title = "Test", posterPath = "/poster.jpg")
        val movie = dto.toMovie()

        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", movie.posterPath)
    }
    @Test
    fun `toMovie with null poster_path does not crash and defaults to empty string`(){
        val dto = MovieDto(id = 1, title = "Test", posterPath = null)
        val movie = dto.toMovie()

        assertEquals("",movie.posterPath)
    }
}
package com.binh.cinetrack.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.sampleMovies

@Composable
fun MoviePoster(movie: Movie,
                modifier:Modifier = Modifier
) {
    val backgroundColor = Color(
        (movie.id * 0xFFFFB800L) or 0xFF000000L)
        Box(
            modifier = modifier.size(120.dp, 180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = movie.title.firstOrNull()?.toString() ?: "",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

@Preview(name= "Movie Poster Light", showBackground = true)
@Preview(name = "Movie Poster Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true)
@Composable
fun MoviePosterPreview(){
    CineTrackTheme(dynamicColor = false) {
        MoviePoster(movie = sampleMovies.first())
    }
}
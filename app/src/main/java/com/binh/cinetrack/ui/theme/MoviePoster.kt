package com.binh.cinetrack.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.binh.cinetrack.R
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.sampleMovies

@Composable
fun MoviePoster(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = movie.posterPath,
        contentDescription = movie.title,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.ic_logo),
        error = painterResource(id = R.drawable.ic_logo)
    )
}

@Preview(name = "Movie Poster Light", showBackground = true)
@Preview(
    name = "Movie Poster Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun MoviePosterPreview() {
    CineTrackTheme(dynamicColor = false) {
        MoviePoster(movie = sampleMovies.first())
    }
}

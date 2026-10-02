package com.binh.cinetrack.ui.theme


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.sampleMovies
import com.binh.cinetrack.domain.top5ByRating

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    allMovies: List<Movie> = sampleMovies,
    onMovieClick: (Movie) -> Unit = {},
    modifier: Modifier = Modifier
) {
    //query tuần 2
    val trendingMovies = remember { allMovies.top5ByRating() }
    val popularMovies = remember { allMovies.sortedBy { it.title } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CineTrack",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            //  Trending (LazyRow)
            item(span = { GridItemSpan(2) }) {
                Column {
                    Text(
                        text = "Trending",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = trendingMovies,
                            key = { movie -> movie.id }
                        ) { movie ->
                            MoviePoster(movie = movie)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Popular",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Grid 2 cột: Popular Movies
            items(
                items = popularMovies,
                key = { movie -> movie.id }
            ) { movie ->
                MovieCard(
                    movie = movie,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Preview(name = "Home Screen Light", showBackground = true)
@Preview(
    name = "Home Screen Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun HomeScreenPreview() {
    CineTrackTheme {
        HomeScreen()
    }
}
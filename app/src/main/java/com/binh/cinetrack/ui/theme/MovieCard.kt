package com.binh.cinetrack.ui.theme


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.sampleMovies



@Composable
fun MovieCard(movie: Movie,
              modifier: Modifier = Modifier,
              onMovieClick:(Movie)-> Unit={}) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = modifier.width(120.dp)
            .clickable { onMovieClick(movie) }
            .padding(4.dp)
        ) {
            MoviePoster(movie = movie)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "★ ${movie.rating}",
                    fontSize = 12.sp,
                    color = Color(0xFFFFB800)
                )
                Text(
                    text = movie.releaseDate.take(4),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
@Preview(name = "Movie Card Light", showBackground = true)
@Preview(name= "Movie Card Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true)
@Composable
fun MovieCardPreview(){
    CineTrackTheme(dynamicColor = false){
        MovieCard(movie = sampleMovies.first())
    }
}
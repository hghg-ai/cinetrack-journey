package com.binh.cinetrack.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.binh.cinetrack.domain.Movie
import com.binh.cinetrack.domain.sampleMovies
import com.binh.cinetrack.domain.search

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen (
    allMovie: List<Movie> = sampleMovies,
    onMovieClick: (Movie)-> Unit = {},
    modifier: Modifier = Modifier
){
    var searchQuery by remember { mutableStateOf("") }
    val searchResult = remember (searchQuery){
        if(searchQuery.isBlank()) emptyList()
        else allMovie.search(searchQuery)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Tìm kiếm", fontWeight = FontWeight.Bold) }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding->
        Column(
            modifier = modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ){
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {searchQuery = it},
                modifier = modifier.fillMaxWidth(),
                placeholder = {Text("Nhập tên phim")},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                trailingIcon ={
                    if(searchQuery.isNotEmpty()){
                        IconButton(onClick = {searchQuery = ""}) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Text"

                            )
                        }
                    }
                },
                singleLine = true
            )
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ){
                when{
                    searchQuery.isBlank()-> {
                        Text(
                            text = "Nhập tên phim ...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = searchResult,
                                key = {movie -> movie.id }
                            ) {movie ->
                                SearchResultItem(
                                    movie = movie,
                                    onClick = {onMovieClick(movie)}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SearchResultItem(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth()
            .clickable{onClick()}
            .padding(vertical = 12.dp ),
        verticalAlignment = Alignment.CenterVertically
    ){
        MoviePoster(
            movie = movie,
            modifier = modifier.size(width = 60.dp, height = 90.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column{
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            val year = movie.releaseDate.substringBefore("-")
            Text(
                text="★ ${movie.rating} · $year",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
package com.binh.cinetrack.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.binh.cinetrack.domain.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen (
    viewModel: SearchViewModel = viewModel() ,
    onMovieClick: (Movie)-> Unit = {},
    modifier: Modifier = Modifier
){
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    //var searchQuery by rememberSaveable() { mutableStateOf("") }
    //val searchResult = remember (searchQuery){
      //  if(searchQuery.isBlank()) emptyList()
        //else allMovie.search(searchQuery)
    //}
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
                onValueChange = {viewModel.onQueryChange(it)},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {Text("Nhập tên phim")},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                trailingIcon ={
                    if(searchQuery.isNotEmpty()){
                        IconButton(onClick = {viewModel.onQueryChange("")}) {
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
                when (val currentState = uiState) {
                    // trạng thái chờ nhập từ khóa
                    is SearchUiState.Idle -> {
                        Text(
                            text = "Nhập tên phim ...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // trạng thái đang đọc mạng
                    is SearchUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    //trạng thái ko tìm thấy kết quả nào
                    is SearchUiState.Empty -> {
                        Text(
                            text = "Không tìm thấy phim nào khớp với từ khóa",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                    // trạng thái lỗi mạng
                    is SearchUiState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text(
                                text = currentState.message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {viewModel.retrySearch()}) {
                                Text(text = "Thử lại")
                            }
                        }
                    }
                    // trạng thái tìm thấy kết quả
                    is SearchUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = currentState.movies,
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
                text = "★ ${movie.rating} · $year",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
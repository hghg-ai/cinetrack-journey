package com.binh.cinetrack.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.binh.cinetrack.domain.sampleGenre
import com.binh.cinetrack.domain.sampleMovies

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    movieId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {// Tra cứu phim từ sampleMovie
    val movie = remember(movieId){ sampleMovies.find { it.id == movieId } }
    //  lưu trạng thái yêu thích
    var isFavorite by remember { mutableStateOf(false) }
// xử lý trường hợp id ko tồn tại
    if (movie == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Chi tiết phim") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại"
                            )
                        }
                    }
                )
            }
        ) { innerpadding ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(innerpadding),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = "không tìm thấy thông tin phim này",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        return
    }
    // Hiển thị thông tin chi tiết khi tìm thấy phim
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(movie.title)},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(onClick = {isFavorite = !isFavorite}) {
                        Icon(
                            imageVector = if(isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Thêm vào yêu thích",
                            tint = if(isFavorite) Color.Red
                            else MaterialTheme.colorScheme.surface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerpadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerpadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box( // Khối Backdrop tỷ lệ 16:9
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(16f/9f)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ){
                // tên phim cỡ headlineMedium
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                // hàng thông tin điểm vs năm
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★ ${movie.rating}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107)
                    )
                    Text(
                        text = "· ${movie.releaseDate.take(4)} ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier= Modifier.height(16.dp))
                // Dải chip thể loại bằng flowrow
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    movie.genreIds.forEach { genreId ->
                        val genreName = sampleGenre.find { it.id == genreId } ?.name?:"Khác"
                        AssistChip(
                            onClick = {},
                            label = {Text(genreName)}
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Đoạn overview đày đủ
                Text(
                    text = "Nội dung phim",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
package com.binh.cinetrack.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import navigation.NavRoute

/**
 * Thành phần UI trung tâm điều hướng toàn bộ ứng dụng CineTrack.
 * Quản lý Navigation Bar, Deep Linking và luồng chuyển màn hình.
 */
@Composable
fun CinetrackApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Biến đếm sự kiện người dùng bấm lại nút Trang chủ để Nạp lại (Refresh)
    var homeRefreshTrigger by remember { mutableIntStateOf(0) }
    // Trạng thái đang tải dữ liệu từ màn Home để kích hoạt hiệu ứng xoay Icon
    var isHomeLoading by remember { mutableStateOf(false) }

    // Hiệu ứng xoay tròn 360 độ liên tục cho Icon Refresh khi đang tải dữ liệu
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Danh sách các route hiển thị thanh điều hướng BottomBar 3 tab
    val bottomBarRoutes = listOf(
        NavRoute.Home,
        NavRoute.Search,
        NavRoute.Favorites
    )
    val showBottomBar = bottomBarRoutes.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            // 1. Hiển thị BottomBar 3 tab chính cho màn Home, Search, Favorites
            if (showBottomBar) {
                NavigationBar {
                    bottomBarRoutes.forEach { screen ->

                        // Tự động đổi Icon Home sang Icon Refresh 🔄 khi đang nạp mạng
                        val iconVector = if (screen == NavRoute.Home && isHomeLoading) {
                            Icons.Default.Refresh
                        } else {
                            screen.icon!!
                        }

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    iconVector,
                                    contentDescription = screen.title,
                                    modifier = if (screen == NavRoute.Home && isHomeLoading) {
                                        Modifier.rotate(rotationAngle)
                                    } else {
                                        Modifier
                                    }
                                )
                            },
                            label = { Text(screen.title!!) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute == screen.route && screen == NavRoute.Home) {
                                    // Đang ở sẵn tab Home mà bấm tiếp -> Tăng biến đếm để nạp lại dữ liệu
                                    homeRefreshTrigger++
                                } else {
                                    // Chuyển tab điều hướng bình thường
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
            // 2. Hiển thị Nút Trang Chủ 🏠 ở chính giữa BottomBar cho màn Detail và GenreScreen
            else if (currentRoute?.startsWith("detail") == true || currentRoute?.startsWith("genre") == true) {
                NavigationBar {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    // Bấm vào -> Xóa các trang trung gian và nhảy thẳng về Trang chủ
                                    navController.popBackStack(NavRoute.Home.route, inclusive = false)
                                }
                                .padding(horizontal = 28.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Trang chủ",
                                modifier = Modifier.size(26.dp)
                            )
                            Text(
                                text = "Trang chủ",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // Sơ đồ điều hướng các màn hình (Navigation Graph)
        NavHost(
            navController = navController,
            startDestination = NavRoute.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) { 
            // Màn hình Trang Chủ (Home)
            composable(NavRoute.Home.route) {
                HomeScreen(
                    homeRefreshTrigger = homeRefreshTrigger,
                    onLoadingStateChanged = { loading -> isHomeLoading = loading },
                    onMovieClick = { movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    },
                    onSettingsClick = {
                        navController.navigate(NavRoute.Settings.route)
                    }
                )
            }
            // Màn hình Cài đặt (Settings)
            composable(NavRoute.Settings.route) {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            // Màn hình Tìm kiếm (Search)
            composable(NavRoute.Search.route) {
                SearchScreen(
                    onMovieClick = { movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    }
                )
            }
            // Màn hình Phim Yêu Thích (Favorites)
            composable(NavRoute.Favorites.route) {
                FavoriteScreen(
                    onMovieClick = { movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    }
                )
            }
            // Màn hình Chi Tiết Phim (Detail) - Đăng ký Deep Link mở thẳng từ Notification
            composable(
                route = NavRoute.Detail.route,
                arguments = listOf(navArgument("movieID") { type = NavType.IntType }),
                deepLinks = listOf(
                    navDeepLink { uriPattern = "cinetrack://detail/{movieID}" }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieID") ?: -1
                DetailScreen(
                    movieId = movieId,
                    onBackClick = { navController.popBackStack() },
                    onGenreClick = { genreId, genreName ->
                        navController.navigate(NavRoute.GenreMovies.createRoute(genreId, genreName))
                    }
                )
            }
            // Màn hình Duyệt Phim Theo Thể Loại (Genre Movies)
            composable(
                route = NavRoute.GenreMovies.route,
                arguments = listOf(
                    navArgument("genreId") { type = NavType.IntType },
                    navArgument("genreName") { type = NavType.StringType }
                )
            ) {
                GenreScreen(
                    onBlackClick = { navController.popBackStack() },
                    onMovieClick = { movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    }
                )
            }
        }
    }
}

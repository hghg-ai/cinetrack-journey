package com.binh.cinetrack.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
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

@Composable
fun CinetrackApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var homeRefreshTrigger by remember { mutableIntStateOf(0) }
    var isHomeLoading by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val bottomBarRoutes = listOf(
        NavRoute.Home,
        NavRoute.Search,
        NavRoute.Favorites
    )
    val showBottomBar = bottomBarRoutes.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomBarRoutes.forEach { screen ->

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
                                    homeRefreshTrigger++
                                } else {
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
                // icon trang chủ ở màn genre và màn detail
                else if (currentRoute?.startsWith("detail")==true || currentRoute?.startsWith("genre") == true){
                    NavigationBar {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable{
                                            navController.popBackStack(NavRoute.Home.route, inclusive = false)
                                        }
                                        .padding(horizontal = 28.dp, vertical = 8.dp)
                                ){
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
        NavHost(
            navController = navController,
            startDestination = NavRoute.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) { // màn home
            composable(NavRoute.Home.route) {
                HomeScreen(
                    homeRefreshTrigger = homeRefreshTrigger,
                    onLoadingStateChanged = {loading -> isHomeLoading = loading},
                    onMovieClick = { movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    },
                    onSettingsClick = {
                        navController.navigate(NavRoute.Settings.route)
                    }
                )
            }// màn setting
            composable(NavRoute.Settings.route) {
                SettingsScreen(
                    onBackClick = {navController.popBackStack()}
                )
            } // màn search
            composable(NavRoute.Search.route) {
                SearchScreen(
                    onMovieClick = { movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    }
                )
            } // màn favorite
            composable(NavRoute.Favorites.route) {
                FavoriteScreen(
                    onMovieClick = {movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    }
                )
            } // màn detail
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
                    },
                )
            }
            // Màn genremovie
                composable(
                    route = NavRoute.GenreMovies.route,
                    arguments = listOf(
                        navArgument("genreId"){type = NavType.IntType},
                        navArgument("genreName") {type = NavType.StringType}
                    )
                ) {
                    GenreScreen(
                        onBlackClick = { navController.popBackStack()},
                        onMovieClick = { movie ->
                            navController.navigate(NavRoute.Detail.createRoute(movie.id))
                        },
                    )
                }
        }
    }
}

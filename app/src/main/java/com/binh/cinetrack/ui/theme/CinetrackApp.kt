package com.binh.cinetrack.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import navigation.NavRoute


@Composable
fun CinetrackApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        NavRoute.Home,
        NavRoute.Search,
        NavRoute.Favorites
    )
val showBottomBar = bottomBarRoutes.any{it.route == currentRoute}

    Scaffold(
        bottomBar = {
            if (showBottomBar){
                NavigationBar{
                    bottomBarRoutes.forEach { screen ->
                        NavigationBarItem(
                            icon = {Icon(screen.icon!!, contentDescription = screen.title)},
                            label = {Text(screen.title!!)},
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route){
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(NavRoute.Home.route){
                HomeScreen(
                    onMovieClick = {movie ->
                        navController.navigate(NavRoute.Detail.createRoute(movie.id))
                    }
                )
            }
            composable(NavRoute.Search.route){
                Text(text = "Màn hình search")
            }
            composable(NavRoute.Favorites.route){
                Text(text = "Màn hình favorite")
            }
            composable(
                route = NavRoute.Detail.route,
                arguments = listOf(navArgument("movieID"){type = NavType.IntType})
            ){backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId")?: -1
                Text(text = "Chi tiết phim ID: $movieId")
            }

        }
    }



}
package com.example.movieapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapp.presentation.screens.FavouritesScreen
import com.example.movieapp.presentation.screens.HomeScreen
import com.example.movieapp.presentation.screens.MovieDetailsScreen
import com.example.movieapp.presentation.screens.SearchScreen
import com.example.movieapp.presentation.viewmodels.FavouritesViewModel
import com.example.movieapp.presentation.viewmodels.HomeViewModel
import com.example.movieapp.presentation.viewmodels.MovieDetailsViewModel
import com.example.movieapp.presentation.viewmodels.SearchViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val favouritesViewModel = hiltViewModel<FavouritesViewModel>()
    val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
    val searchViewModel = hiltViewModel<SearchViewModel>()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Details) {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedItem == 0,
                        onClick = { selectedItem = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = selectedItem == 1,
                        onClick = { selectedItem = 1 },
                        icon = {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                contentDescription = "Favorites"
                            )
                        },
                        label = { Text("Favorites") }
                    )
                    NavigationBarItem(
                        selected = selectedItem == 2,
                        onClick = { selectedItem = 2 },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        label = { Text("Search") }
                    )
                }
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable(Screen.Home) {
                HomeScreen(viewModel = homeViewModel) { movieId ->
                    navController.navigate(Screen.detailsRoute(movieId))
                }
            }
            composable(Screen.Favorites) {
                FavouritesScreen(viewModel = favouritesViewModel) { movieId ->
                    navController.navigate(Screen.detailsRoute(movieId))
                }
            }
            composable(Screen.Search) {
                SearchScreen(viewModel = searchViewModel) { movieId ->
                    navController.navigate(Screen.detailsRoute(movieId))
                }
            }
            composable(
                route = Screen.Details,
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                MovieDetailsScreen(
                    viewModel = movieDetailsViewModel,
                    movieId = movieId,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

    LaunchedEffect(selectedItem) {
        when (selectedItem) {
            0 -> navController.navigate(Screen.Home) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }

            1 -> navController.navigate(Screen.Favorites) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }

            2 -> navController.navigate(Screen.Search) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }
}

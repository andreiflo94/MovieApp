package com.example.movieapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.movieapp.presentation.common.UiState
import com.example.movieapp.presentation.common.MovieGrid
import com.example.movieapp.presentation.viewmodels.FavouritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel,
    onMovieClick: (Int) -> Unit
) {
    val favouritesState = viewModel.favouritesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favourites") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = favouritesState.value) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message)
                    }
                }

                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No favourites yet")
                        }
                    } else {
                        MovieGrid(
                            movies = state.data,
                            onMovieClick = onMovieClick,
                            onFavouriteClick = { movieId, isFavourite ->
                                viewModel.updateFavourite(movieId, isFavourite)
                            }
                        )
                    }
                }
            }
        }
    }
}

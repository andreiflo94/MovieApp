package com.example.movieapp.presentation.common

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.models.Movie

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MovieGrid(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavouriteClick: (Int, Boolean) -> Unit,
    // Optional — only provided when navigating from screens that support
    // shared element transitions (e.g. Home). Favourites/Search still work
    // without them; they just won't animate.
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp)
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieGridItem(
                movie = movie,
                onClick = { onMovieClick(movie.id) },
                onFavouriteClick = onFavouriteClick,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieGridItem(
    movie: Movie,
    onClick: () -> Unit,
    onFavouriteClick: (Int, Boolean) -> Unit = { _, _ -> },
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Build the shared-element modifier when both scopes are present.
        // The key matches the one used in MovieDetailsScreen so Compose can
        // connect the two instances and animate between them.
        val posterModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
            with(sharedTransitionScope) {
                Modifier.sharedElement(
                    state = rememberSharedContentState(key = "movie_poster_${movie.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        } else {
            Modifier
        }

        Image(
            painter = rememberAsyncImagePainter(movie.posterUrl),
            contentDescription = movie.title,
            modifier = posterModifier
                .height(220.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = movie.releaseYear,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "${movie.averageRating} ★",
                modifier = Modifier.padding(end = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onFavouriteClick(movie.id, !movie.isFavourite) }) {
                Icon(
                    imageVector = if (movie.isFavourite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = if (movie.isFavourite) "Remove from favourites"
                    else "Add to favourites",
                    tint = if (movie.isFavourite) Color.Red else Color.Gray
                )
            }
        }
    }
}
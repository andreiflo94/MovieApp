package com.example.movieapp.presentation.common

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.models.Movie

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieGrid(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavouriteClick: (Int, Boolean) -> Unit
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
                movie = movie, onClick = { onMovieClick(movie.id) },
                onFavouriteClick = onFavouriteClick
            )
        }
    }
}

@Composable
fun MovieGridItem(
    movie: Movie,
    onClick: () -> Unit,
    onFavouriteClick: (Int, Boolean) -> Unit = { _, _ -> }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.posterUrl),
            contentDescription = movie.title,
            modifier = Modifier
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
                    imageVector = if (movie.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (movie.isFavourite) "Remove from favourites" else "Add to favourites",
                    tint = if (movie.isFavourite) Color.Red else Color.Gray
                )
            }
        }
    }
}

// region previews

class SampleMovieProvider : PreviewParameterProvider<Movie> {
    override val values = sequenceOf(
        Movie(
            id = 1,
            title = "Inception",
            posterUrl = "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
            releaseYear = "2010",
            averageRating = 8.8,
            isFavourite = false
        ),
        Movie(
            id = 2,
            title = "The Dark Knight",
            posterUrl = "https://image.tmdb.org/t/p/w500/1hRoyzDtpgMU7Dz4JF22RANzQO7.jpg",
            releaseYear = "2008",
            averageRating = 9.0,
            isFavourite = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun MovieGridItemPreview(
    @PreviewParameter(SampleMovieProvider::class) movie: Movie
) {
    MovieGridItem(
        movie = movie,
        onClick = {},
        onFavouriteClick = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun MovieGridPreview() {
    val movies = listOf(
        Movie(
            id = 1,
            title = "Inception",
            posterUrl = "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
            releaseYear = "2010",
            averageRating = 8.8,
            isFavourite = false
        ),
        Movie(
            id = 2,
            title = "The Dark Knight",
            posterUrl = "https://image.tmdb.org/t/p/w500/1hRoyzDtpgMU7Dz4JF22RANzQO7.jpg",
            releaseYear = "2008",
            averageRating = 9.0,
            isFavourite = true
        )
    )
    MovieGrid(
        movies = movies,
        onMovieClick = {},
        onFavouriteClick = { _, _ -> }
    )
}

// endregion
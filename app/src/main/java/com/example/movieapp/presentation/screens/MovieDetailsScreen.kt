package com.example.movieapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.presentation.common.UiState
import com.example.movieapp.presentation.viewmodels.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel,
    movieId: Int,
    onBack: () -> Unit
) {
    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    val state by viewModel.movieDetailsState.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Error -> Text(
                    text = (state as UiState.Error).message,
                    modifier = Modifier.align(Alignment.Center)
                )

                is UiState.Success -> MovieDetailsContent(
                    (state as UiState.Success<MovieDetails>).data,
                    onBack,
                    updateFavourite = { movieId, isFavourite ->
                        viewModel.updateFavourite(movieId, isFavourite)
                    }
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: MovieDetails,
    onBack: () -> Unit,
    updateFavourite: (Int, Boolean) -> Unit = { _, _ -> },
) {
    var isFavourite by rememberSaveable { mutableStateOf(movie.isFavourite) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MovieDetailsHeader(movie, onBack)
        Surface(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                MovieDetailsPosterSection(movie, isFavourite) {
                    updateFavourite(movie.id, !isFavourite)
                    isFavourite = !isFavourite
                }
                MovieDetailsOverview(movie)
            }
        }
    }
}

@Composable
private fun MovieDetailsHeader(movie: MovieDetails, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = rememberAsyncImagePainter(movie.backdropPath),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(32.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
private fun MovieDetailsPosterSection(
    movie: MovieDetails,
    isFavourite: Boolean,
    onToggleFavourite: () -> Unit
) {
    Row(modifier = Modifier.padding(top = 16.dp)) {
        Image(
            painter = rememberAsyncImagePainter(movie.posterPath),
            contentDescription = movie.title,
            modifier = Modifier
                .padding(start = 16.dp)
                .width(120.dp)
                .height(170.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    if (!movie.tagline.isNullOrBlank()) {
                        Text(
                            text = movie.tagline,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                IconButton(onClick = onToggleFavourite) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavourite) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MovieDetailsStats(movie)

            Spacer(modifier = Modifier.height(4.dp))

            MovieDetailsGenres(movie)
        }
    }
}


@Composable
private fun MovieDetailsStats(movie: MovieDetails) {
    Column(modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = String.format("%.1f ★", movie.voteAverage),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "(${movie.voteCount})", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = movie.releaseDate, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MovieDetailsGenres(movie: MovieDetails) {
    if (movie.genres.isNotBlank()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 4.dp, end = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            movie.genres.split(",").forEach { genre ->
                AssistChip(onClick = {}, label = { Text(genre) })
            }
        }
    }
}

@Composable
private fun MovieDetailsOverview(movie: MovieDetails) {
    if (movie.overview.isNotBlank()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 8.dp)
        ) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// region previews

private val sampleMovieDetails = MovieDetails(
    id = 1,
    title = "The Shawshank Redemption",
    tagline = "Fear can hold you prisoner. Hope can set you free.",
    overview = "Framed in the 1940s for double murder, upstanding banker Andy Dufresne begins a new life at Shawshank prison.",
    posterPath = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
    backdropPath = "https://image.tmdb.org/t/p/w500/xBKGJQsAIeweesB79KC89FpBrVr.jpg",
    voteAverage = 9.3,
    voteCount = 25000,
    releaseDate = "1994-09-23",
    genres = "Drama, Crime",
    isFavourite = false
)


@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenPreview() {
    MovieDetailsContent(
        movie = sampleMovieDetails,
        onBack = {},
        updateFavourite = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsHeaderPreview() {
    MovieDetailsHeader(movie = sampleMovieDetails, onBack = {})
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsPosterSectionPreview() {
    MovieDetailsPosterSection(
        movie = sampleMovieDetails,
        isFavourite = false,
        onToggleFavourite = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsStatsPreview() {
    MovieDetailsStats(movie = sampleMovieDetails)
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsGenresPreview() {
    MovieDetailsGenres(movie = sampleMovieDetails)
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsOverviewPreview() {
    MovieDetailsOverview(movie = sampleMovieDetails)
}

// endregion
package com.example.movieapp.data.mappers

import com.example.movieapp.data.localdb.entity.MovieDetailsEntity
import com.example.movieapp.data.localdb.entity.MovieEntity
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieDetails

fun MovieEntity.toDomain() = Movie(
    id = id,
    title = title,
    posterUrl = posterUrl,
    releaseYear = releaseYear,
    averageRating = averageRating,
    isFavourite = isFavourite
)

fun MovieDetailsEntity.toDomain() = MovieDetails(
    id = id,
    title = title,
    posterPath = posterUrl,
    backdropPath = backdropUrl,
    tagline = tagline,
    releaseDate = releaseYear,
    voteAverage = averageRating,
    voteCount = voteCount,
    overview = overview,
    genres = genres,
    isFavourite = isFavourite
)

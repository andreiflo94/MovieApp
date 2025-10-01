package com.example.movieapp.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val tagline: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val overview: String,
    val genres: String,
    val isFavourite: Boolean
)
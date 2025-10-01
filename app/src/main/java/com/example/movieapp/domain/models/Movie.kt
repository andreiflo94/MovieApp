package com.example.movieapp.domain.models

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val releaseYear: String,
    val averageRating: Double,
    val isFavourite: Boolean,
)
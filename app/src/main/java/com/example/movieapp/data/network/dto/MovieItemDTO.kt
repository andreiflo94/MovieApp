package com.example.movieapp.data.network.dto

data class MovieItemDTO(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val release_date: String,
    val vote_average: Double
)
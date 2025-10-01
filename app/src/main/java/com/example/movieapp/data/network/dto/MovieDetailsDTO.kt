package com.example.movieapp.data.network.dto


data class MovieDetailsDTO(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val tagline: String?,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val overview: String,
    val genres: List<GenreDTO>
)

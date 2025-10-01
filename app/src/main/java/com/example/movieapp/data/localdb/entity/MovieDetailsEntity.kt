package com.example.movieapp.data.localdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moviedetails")
data class MovieDetailsEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val tagline: String?,
    val releaseYear: String,
    val averageRating: Double,
    val voteCount: Int,
    val overview: String,
    val genres: String, // Comma-separated list of genre names
    val isFavourite: Boolean = false
)

package com.example.movieapp.data.localdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String?,
    val releaseYear: String,
    val averageRating: Double,
    val type: String,
    val isFavourite: Boolean,
)

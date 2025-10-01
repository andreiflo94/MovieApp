package com.example.movieapp.domain.repository

import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesByType(type: String): Flow<List<Movie>>
    fun getFavouriteMovies(): Flow<List<Movie>>
    suspend fun refreshMovies(type: String)
    suspend fun getMovieDetails(id: Int): MovieDetails?
    suspend fun updateFavourite(movieId: Int, isFavourite: Boolean)
    fun searchMovies(query: String): Flow<List<Movie>>
}

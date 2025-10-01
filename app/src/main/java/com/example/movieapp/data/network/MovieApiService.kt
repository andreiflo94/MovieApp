package com.example.movieapp.data.network

import com.example.movieapp.data.network.dto.MovieDetailsDTO
import com.example.movieapp.data.network.dto.MovieListResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiService {

    // Get recommendations based on type (now_playing, popular, top_rated, upcoming)
    @GET("movie/{recommendation_type}")
    suspend fun getMoviesByType(
        @Path("recommendation_type") type: String
    ): Response<MovieListResponseDTO>

    // Get movie details by ID
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailsDTO>
}
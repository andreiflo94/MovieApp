package com.example.movieapp.presentation.navigation

object Screen {
    const val Home = "home"
    const val Details = "details/{movieId}"
    const val Favorites = "favorites"
    const val Search = "search"
    fun detailsRoute(movieId: Int) = "details/$movieId"
}
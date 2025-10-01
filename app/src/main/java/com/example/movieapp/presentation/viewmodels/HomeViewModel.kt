package com.example.movieapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    companion object {
        val tabs = listOf("now_playing", "popular", "top_rated", "upcoming")
    }

    init {
        refreshMovies()
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            tabs.forEach { type ->
                repository.refreshMovies(type)
            }
        }
    }

    val nowPlayingState: StateFlow<UiState<List<Movie>>> =
        repository.getMoviesByType(tabs[0])
            .map { UiState.Success(it) as UiState<List<Movie>> }
            .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )

    val popularState: StateFlow<UiState<List<Movie>>> =
        repository.getMoviesByType(tabs[1])
            .map { UiState.Success(it) as UiState<List<Movie>> }
            .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )

    val topRatedState: StateFlow<UiState<List<Movie>>> =
        repository.getMoviesByType(tabs[2])
            .map { UiState.Success(it) as UiState<List<Movie>> }
            .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )

    val upcomingState: StateFlow<UiState<List<Movie>>> =
        repository.getMoviesByType(tabs[3])
            .map { UiState.Success(it) as UiState<List<Movie>> }
            .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )


    fun updateFavourite(movieId: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.updateFavourite(movieId, isFavourite)
        }
    }
}

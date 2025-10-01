package com.example.movieapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieDetailsState = MutableStateFlow<UiState<MovieDetails>>(UiState.Loading)
    val movieDetailsState: StateFlow<UiState<MovieDetails>> = _movieDetailsState.asStateFlow()

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _movieDetailsState.value = UiState.Loading
            try {
                val details = repository.getMovieDetails(movieId)
                if (details != null) {
                    _movieDetailsState.value = UiState.Success(details)
                } else {
                    _movieDetailsState.value = UiState.Error("Movie not found")
                }
            } catch (e: Exception) {
                _movieDetailsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateFavourite(movieId: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.updateFavourite(movieId, isFavourite)
        }
    }
}

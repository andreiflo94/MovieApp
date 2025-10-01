package com.example.movieapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _favouritesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val favouritesState: StateFlow<UiState<List<Movie>>> = _favouritesState.asStateFlow()

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            repository.getFavouriteMovies()
                .onStart {
                    _favouritesState.value = UiState.Loading
                }
                .catch { e ->
                    _favouritesState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
                }
                .collect { movies ->
                    _favouritesState.value = UiState.Success(movies)
                }
        }
    }

    fun updateFavourite(movieId: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.updateFavourite(movieId, isFavourite)
        }
    }

}

package com.example.movieapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults =
        MutableStateFlow<UiState<List<Movie>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<Movie>>> = _searchResults.asStateFlow()

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(emptyList())
                    } else {
                        repository.searchMovies(query)
                            .onStart { _searchResults.value = UiState.Loading }
                            .catch { e ->
                                _searchResults.value = UiState.Error(e.message ?: "Unknown error")
                            }
                    }
                }
                .collect { results ->
                    _searchResults.value = UiState.Success(results)
                }
        }
    }

    fun updateFavourite(movieId: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.updateFavourite(movieId, isFavourite)
        }
    }
}

package com.example.movieapp.data.repository

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.localdb.dao.MovieDao
import com.example.movieapp.data.localdb.entity.MovieDetailsEntity
import com.example.movieapp.data.localdb.entity.MovieEntity
import com.example.movieapp.data.mappers.toDomain
import com.example.movieapp.data.network.MovieApiService
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val api: MovieApiService,
    private val dao: MovieDao
) : MovieRepository {

    override fun getFavouriteMovies(): Flow<List<Movie>> =
        dao.getFavouriteMovies()
            .map { list -> list.map { it.toDomain() } }

    override fun getMoviesByType(type: String): Flow<List<Movie>> =
        dao.getMoviesByType(type).map { list -> list.map { it.toDomain() } }
            .catch { e ->
                emit(emptyList())
            }

    override suspend fun refreshMovies(type: String) {
        try {
            val response = api.getMoviesByType(type)
            if (response.isSuccessful) {
                response.body()?.results?.let { dtoList ->
                    val entities = dtoList.map {
                        MovieEntity(
                            id = it.id,
                            title = it.title,
                            posterUrl = BuildConfig.IMAGE_BASE_URL + it.poster_path,
                            releaseYear = it.release_date.take(4),
                            averageRating = it.vote_average,
                            type = type,
                            isFavourite = dao.getMovieById(it.id)?.isFavourite ?: false
                        )
                    }
                    dao.insertMovies(entities)
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to refresh movies", e)
        }
    }

    override suspend fun getMovieDetails(id: Int): MovieDetails? {
        return try {
            dao.getMovieDetails(id)?.toDomain() ?: fetchAndCacheMovieDetails(id)
        } catch (e: Exception) {
            throw RuntimeException("Failed to fetch movie details", e)
        }
    }

    private suspend fun fetchAndCacheMovieDetails(id: Int): MovieDetails? {
        val response = api.getMovieDetails(id)
        return if (response.isSuccessful) {
            response.body()?.let { dto ->
                val entity = MovieDetailsEntity(
                    id = dto.id,
                    title = dto.title,
                    posterUrl = BuildConfig.IMAGE_BASE_URL + dto.poster_path,
                    backdropUrl = BuildConfig.IMAGE_BASE_URL + dto.backdrop_path,
                    tagline = dto.tagline,
                    releaseYear = dto.release_date.take(4),
                    averageRating = dto.vote_average,
                    voteCount = dto.vote_count,
                    overview = dto.overview,
                    genres = dto.genres.joinToString(",") { it.name },
                    isFavourite = dao.getMovieById(dto.id)?.isFavourite ?: false
                )
                dao.insertMovieDetails(entity)
                entity.toDomain()
            }
        } else null
    }

    override suspend fun updateFavourite(movieId: Int, isFavourite: Boolean) {
        try {
            dao.updateFavourite(movieId, isFavourite)
            dao.updateMovieDetailsFavourite(movieId, isFavourite)
        } catch (e: Exception) {
            throw RuntimeException("Failed to update favourite status", e)
        }
    }

    override fun searchMovies(query: String): Flow<List<Movie>> = dao.searchMovies(query)
        .map { list -> list.map { it.toDomain() } }
}

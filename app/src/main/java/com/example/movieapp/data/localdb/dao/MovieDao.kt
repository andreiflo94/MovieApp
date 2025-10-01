package com.example.movieapp.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.localdb.entity.MovieDetailsEntity
import com.example.movieapp.data.localdb.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE isFavourite = 1")
    fun getFavouriteMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE type = :type")
    fun getMoviesByType(type: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    fun searchMovies(query: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM moviedetails WHERE id = :movieId")
    suspend fun getMovieDetails(movieId: Int): MovieDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(details: MovieDetailsEntity)

    @Query("UPDATE movies SET isFavourite = :isFavourite WHERE id = :movieId")
    suspend fun updateFavourite(movieId: Int, isFavourite: Boolean)

    @Query("UPDATE moviedetails SET isFavourite = :isFavourite WHERE id = :movieId")
    suspend fun updateMovieDetailsFavourite(movieId: Int, isFavourite: Boolean)
}

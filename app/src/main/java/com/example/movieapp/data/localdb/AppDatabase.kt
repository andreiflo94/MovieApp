package com.example.movieapp.data.localdb


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.data.localdb.dao.MovieDao
import com.example.movieapp.data.localdb.entity.MovieDetailsEntity
import com.example.movieapp.data.localdb.entity.MovieEntity

@Database(
    entities = [MovieEntity::class, MovieDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
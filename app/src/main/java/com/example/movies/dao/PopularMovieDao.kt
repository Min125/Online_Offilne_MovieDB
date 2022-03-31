package com.example.movies.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.model.PopularDB

@Dao
interface PopularMovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun savePopularData( popularDB: PopularDB)


    @Query(value = "Select * FROM popular_movie_table")
    fun getPopularDataFromDB() : LiveData<List<PopularDB>>

    @Query(value = "Select * FROM popular_movie_table WHERE id = :id")
    fun getPopularById(id : Int) : LiveData<PopularDB>

}
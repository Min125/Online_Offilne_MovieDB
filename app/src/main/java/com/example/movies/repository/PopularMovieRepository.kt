package com.example.movies.repository

import androidx.lifecycle.LiveData
import com.example.movies.dao.PopularMovieDao
import com.example.movies.model.PopularDB

class PopularMovieRepository (private val popularMovieDao : PopularMovieDao){


    val allPopularMovies : LiveData<List<PopularDB>> = popularMovieDao.getPopularDataFromDB()

    suspend fun insertPopularMoviesData (popularDB: PopularDB){
        popularMovieDao.savePopularData(popularDB)
    }

    fun getDetailData (id : Int) :LiveData<PopularDB>{
        return popularMovieDao.getPopularById(id)
    }

}
package com.example.movies.network

import com.example.movies.model.DetailMovie
import com.example.movies.model.PopularMovies
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NetworkClient {

    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val API_KEY = "1e5e734fc389f188995e3839bf1910f5"
    val networkInterface : NetworkInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    fun getPopularMovies () : Call<PopularMovies>{
        return networkInterface.getPopularMovies(API_KEY)
    }

    fun getDetailMovie (id : Int): Call<DetailMovie>{
        return networkInterface.getDetailMovie(id,API_KEY)
    }
}
package com.example.movies.network

import com.example.movies.model.DetailMovie
import com.example.movies.model.PopularMovies
import com.example.movies.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkInterface {

    @GET("movie/popular")
    fun getPopularMovies (
        @Query("api_key") api_key : String
    ) : Call<PopularMovies>

    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Path("movie_id") movie_id : Int,
        @Query("api_key") api_key : String
    ) : Call<DetailMovie>


}
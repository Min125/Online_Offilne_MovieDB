package com.example.movies.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.movies.database.PopularMovieDatabase
import com.example.movies.model.DetailMovie
import com.example.movies.model.PopularDB
import com.example.movies.model.PopularMovies
import com.example.movies.network.NetworkClient
import com.example.movies.repository.PopularMovieRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel(application: Application) : AndroidViewModel(application){


    private var popularMovies: MutableLiveData<PopularMovies> = MutableLiveData()
    private var movieDetail : MutableLiveData<DetailMovie> = MutableLiveData()

    var popularMoviesFromDB : LiveData<List<PopularDB>>

    private val popularMovieRepository : PopularMovieRepository

    var isFinishDetail : Boolean = false
    var isFinishPopular : Boolean = false

    var movieId : Int =0

    init {
        val popularMovieDao = PopularMovieDatabase.getDatabase(
            application
        ).popularMovieDao()


        popularMovieRepository = PopularMovieRepository(popularMovieDao)

        popularMoviesFromDB = popularMovieRepository.allPopularMovies
    }

    fun getPopularMovies() : LiveData<PopularMovies> = popularMovies
    fun getMovieDeatil() : LiveData<DetailMovie> = movieDetail

    //Detail Movie from database
    fun getDetailMovieFromDB(id : Int) : LiveData<PopularDB> {
        return popularMovieRepository.getDetailData(id)
    }

    //Save data to popular movie database
    fun savePopularMoviesToDB (popularDB: PopularDB) = viewModelScope.launch{
        popularMovieRepository.insertPopularMoviesData(popularDB)
    }

    fun getLoadingPopularMovie(){
        isFinishDetail = false
        var networkClient = NetworkClient()
        var call = networkClient.getPopularMovies()
        call.enqueue(object : Callback<PopularMovies>{
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                isFinishDetail = true
                popularMovies.value = response.body()
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Log.d("Error>>>",t.toString())
            }

        })
    }

    fun getLoadingMovieDetail (id : Int){
        var networlClient = NetworkClient()
        var call = networlClient.getDetailMovie(id)
        call.enqueue(object : Callback<DetailMovie>{
            override fun onResponse(call: Call<DetailMovie>, response: Response<DetailMovie>) {
                isFinishPopular = true
                movieDetail.value = response.body()
            }

            override fun onFailure(call: Call<DetailMovie>, t: Throwable) {
                Log.d("Error>>>",t.toString())
            }

        })
    }
}
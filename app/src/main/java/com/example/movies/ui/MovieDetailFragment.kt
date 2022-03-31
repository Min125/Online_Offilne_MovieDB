package com.example.movies.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.movies.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_detail.*

class MovieDetailFragment : Fragment() {

    private val moviesViewModel : MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back.setOnClickListener {
            this.findNavController().navigateUp()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        setDetailMovieView()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setDetailMovieView (){
        //Online
        if (isOnline(requireContext())){
            moviesViewModel.getLoadingMovieDetail(moviesViewModel.movieId)
            moviesViewModel.getMovieDeatil().observe(viewLifecycleOwner, Observer {
                if (!it.backdrop_path.isNullOrEmpty()){
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/" + it.backdrop_path)
                        .into(img_backdrop)
                }else{
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/" + it.poster_path)
                        .into(img_backdrop)
                }

                movie_name_textView.text = it.original_title

                release_date_textView.text = "Release Date : ${it.release_date}"

                rating_textView.text = "${it.vote_average} / 10"

                summary_textView.text = it.overview

                //Check Loading complete
                if (it.id == moviesViewModel.movieId){
                    layout_movieDetail.isVisible = true
                    progressBar_movieDetail.isVisible = false
                }

            })
        }
        //offline
        else{
            moviesViewModel.getDetailMovieFromDB(moviesViewModel.movieId).observe(viewLifecycleOwner, Observer {

                img_backdrop.load(it.backposter)

                movie_name_textView.text = it.original_title

                release_date_textView.text = "Release Date : ${it.release_date}"

                rating_textView.text = "${it.rating} / 10"

                summary_textView.text = it.summary

                //Check Loading complete
                if (it.id == moviesViewModel.movieId){
                    layout_movieDetail.isVisible = true
                    progressBar_movieDetail.isVisible = false
                }

            })
        }
    }


    override fun onPause() {
        super.onPause()
        layout_movieDetail.isVisible = false
        progressBar_movieDetail.isVisible = true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}
package com.example.movies.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.movies.R
import com.example.movies.model.PopularDB
import com.example.movies.model.Result
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.launch
import java.util.zip.Inflater


class MoviesFragment : Fragment(), PopularAdapter.ClickListener,PopularOfflineAdapter.ClickListener {

    private val movieViewModel : MoviesViewModel by activityViewModels()

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var popularOfflineAdapter: PopularOfflineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movies, container, false)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularAdapter = PopularAdapter()
        popularOfflineAdapter = PopularOfflineAdapter()

        movieViewModel.getLoadingPopularMovie()

        //offline
        movieViewModel.popularMoviesFromDB.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()){
                progressBar_popular.isVisible=false
                popularOfflineAdapter.updateList(it)
            }else{
                layout_noInternet.isVisible = true
            }
        })

        popular_movies_recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = popularOfflineAdapter
        }

        //Check online of offline
        if(isOnline(requireContext())){
            //Online
            movieViewModel.getPopularMovies().observe(viewLifecycleOwner, Observer {popular ->
                layout_noInternet.isVisible = false
                progressBar_popular.isVisible = false
                popularAdapter.updateList(popular.results)

                if (!popular.results.isNullOrEmpty()){
                    var popularMoviesList : ArrayList<PopularDB> = ArrayList()

                    //save popular data in to database
                    for (i in popular.results ){
                        lifecycleScope.launch {
                            if (!i.backdrop_path.isNullOrEmpty()){
                                movieViewModel.savePopularMoviesToDB(
                                    PopularDB(i.id,i.original_title,i.release_date,i.vote_average.toString(),
                                        i.overview,getBitmap("https://image.tmdb.org/t/p/w500/${i.backdrop_path}"),
                                        getBitmap("https://image.tmdb.org/t/p/w500/${i.poster_path}")))
                            }else{
                                movieViewModel.savePopularMoviesToDB(
                                    PopularDB(i.id,i.original_title,i.release_date,i.vote_average.toString(),
                                        i.overview,getBitmap("https://image.tmdb.org/t/p/w500/${i.poster_path}"),
                                        getBitmap("https://image.tmdb.org/t/p/w500/${i.poster_path}")))
                            }
                        }
                    }
                }

                popular_movies_recyclerView.apply {
                    layoutManager = GridLayoutManager(context, 3)
                    adapter = popularAdapter
                }
            })
        }

        popularOfflineAdapter.setOnClickListener(this)
        popularAdapter.setOnClickListener(this)

    }

    override fun onClick(result: Result) {
        movieViewModel.movieId = result.id
        this.findNavController().navigate(R.id.fragment_to_movie_detail)
    }

    private suspend fun getBitmap (url : String) :Bitmap{
        var loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
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

    override fun onOfflineClick(result: PopularDB) {
        movieViewModel.movieId = result.id
        this.findNavController().navigate(R.id.fragment_to_movie_detail)
    }

}
package com.example.movies.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.model.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_popular_movie.view.*

class PopularAdapter : RecyclerView.Adapter<PopularAdapter.PopularViewModel>(){

    private var popularList: List<Result> = ArrayList()

    var clickListener : ClickListener? = null

    inner class PopularViewModel(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        lateinit var result: Result

        fun bind (result: Result){
            this.result = result
            itemView.popular_movie_name_textView.text = result.original_title
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/" + result.poster_path)
                .into(itemView.img_popular)
        }

        override fun onClick(p0: View?) {
            clickListener?.onClick(result)
        }
    }

    fun updateList(popularList: List<Result>) {
        this.popularList = popularList
        notifyDataSetChanged()

    }


    fun setOnClickListener(clickListener : ClickListener){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewModel {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_movie,parent,false)
        return PopularViewModel(view)
    }

    override fun onBindViewHolder(holder: PopularViewModel, position: Int) {
        holder.bind(popularList[position])
    }

    override fun getItemCount(): Int = popularList.size

    interface ClickListener{
        fun onClick(result: Result)
    }

}
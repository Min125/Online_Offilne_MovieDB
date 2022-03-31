package com.example.movies.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movies.R
import com.example.movies.model.PopularDB
import com.example.movies.model.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_popular_movie.view.*

class PopularOfflineAdapter : RecyclerView.Adapter<PopularOfflineAdapter.PopularOfflineViewModel>(){

    private var popularList: List<PopularDB> = ArrayList()

    var clickListener : ClickListener? = null

    inner class PopularOfflineViewModel(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        lateinit var result: PopularDB

        fun bind (result: PopularDB){
            this.result = result
            itemView.popular_movie_name_textView.text = result.original_title
            itemView.img_popular.load(result.poster)
        }

        override fun onClick(p0: View?) {
            clickListener?.onOfflineClick(result)
        }
    }

    fun updateList(popularList: List<PopularDB>) {
        this.popularList = popularList
        notifyDataSetChanged()

    }


    fun setOnClickListener(clickListener : ClickListener){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularOfflineViewModel {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_movie,parent,false)
        return PopularOfflineViewModel(view)
    }

    override fun onBindViewHolder(holder: PopularOfflineViewModel, position: Int) {
        holder.bind(popularList[position])
    }

    override fun getItemCount(): Int = popularList.size

    interface ClickListener{
        fun onOfflineClick(result: PopularDB)
    }

}